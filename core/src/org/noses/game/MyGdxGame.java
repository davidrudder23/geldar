package org.noses.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import org.noses.game.character.Character;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter implements ApplicationListener, GestureListener, InputProcessor {
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	private OrthographicCamera camera;

	private int tilePixelWidth;
	private int tilePixelHeight;

	Character avatar;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();
		tiledMap = new TmxMapLoader().load("game.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		MapProperties prop = tiledMap.getProperties();
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);

		avatar = new Character("avatar.png");

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		recenterCamera();
		
	    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
	    cell.setTile(new StaticTiledMapTile(avatar.getAnimation()[avatar.getDirection()][avatar.getFrame()]));
	    TiledMapTileLayer avatarLayer = getAvatarLayer();
	    avatarLayer.setCell(avatar.getX(), avatar.getY(), cell);
	    
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
	}

	private void recenterCamera() {
		float camCenterX = avatar.getX();
		float camCenterY = avatar.getY();

		float camHeightInTiles = (camera.viewportHeight/2)/tilePixelHeight;
		float camWidthInTiles = (camera.viewportWidth/2)/tilePixelWidth;
		
		System.out.println("camHeightInTiles="+camHeightInTiles);
		System.out.println("camWidthInTiles="+camWidthInTiles);
		System.out.println("avatar.getX()="+avatar.getX());
		System.out.println("avatar.getY()="+avatar.getY());

		if (avatar.getX() < camWidthInTiles) camCenterX = camWidthInTiles;
		if (avatar.getY() < camHeightInTiles) camCenterY = camHeightInTiles;

		float rightBoundary = getAvatarLayer().getWidth()-camWidthInTiles;
		if (avatar.getX() > rightBoundary) camCenterX = rightBoundary;

		float bottomBoundary = getAvatarLayer().getHeight()-camHeightInTiles;
		if (avatar.getY() > bottomBoundary) camCenterY = bottomBoundary;

		System.out.println("camCenterX="+camCenterX);
		System.out.println("camCenterY="+camCenterY);

		camera.position.set(camCenterX*tilePixelWidth, camCenterY*tilePixelHeight, 0);

		camera.update();
	}

	private boolean isCollision(int moveHoriz, int moveVert) {
		int left = avatar.getX()+moveHoriz;
		int top = avatar.getY()+moveVert;

		List<TiledMapTileLayer> obstructionLayers = getObstructionLayers();

		for (TiledMapTileLayer ml: obstructionLayers) {
			if (ml.getCell(left, top) != null) {
				return true;
			}
		}
		return false;
	}

	private boolean isOffScreen(int moveHoriz, int moveVert) {
		int newTileX = avatar.getX() + moveHoriz;
		int newTileY = avatar.getY() + moveVert;

		if (newTileX < 0) return true;
		if (newTileX >= getAvatarLayer().getWidth()) return true;
		if (newTileY < 0) return true;
		if (newTileY >= getAvatarLayer().getHeight()) return true;

		return false;
	}
	private boolean isMovementBlocked(int moveHoriz, int moveVert) {
		if (isOffScreen(moveHoriz, moveVert)) return true;
		if (isCollision(moveHoriz, moveVert)) return true;
		return false;
	}

	private List<TiledMapTileLayer> getObstructionLayers() {
		List<TiledMapTileLayer> obstructionLayers = new ArrayList<>();
		MapLayers mapLayers = tiledMap.getLayers();
		for (int i = 0; i < mapLayers.getCount(); i++) {
			MapLayer mapLayer = mapLayers.get(i);
			if ((mapLayer.getName().toLowerCase().contains("obstruction")) && (mapLayer instanceof TiledMapTileLayer)) {
				TiledMapTileLayer tmtl = (TiledMapTileLayer) mapLayer;
				obstructionLayers.add(tmtl);
			}
		}

		return obstructionLayers;
	}
	
	private TiledMapTileLayer getAvatarLayer() {
		MapLayers mapLayers = tiledMap.getLayers();
		for (int i = 0; i < mapLayers.getCount(); i++) {
			MapLayer mapLayer = mapLayers.get(i);
			if ((mapLayer.getName().toLowerCase().contains("walking")) && (mapLayer instanceof TiledMapTileLayer)) {
				TiledMapTileLayer tmtl = (TiledMapTileLayer) mapLayer;
				return tmtl;
			}
		}

		return null;
	}

	@Override
	public void resize(int width, int height) {
		// stage.getViewport().update(width, height, true);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
	    TiledMapTileLayer avatarLayer = getAvatarLayer();
	    avatarLayer.setCell(avatar.getX(), avatar.getY(), null);

		if (keycode == Input.Keys.LEFT) {
            avatar.setDirection(2);
			if (isMovementBlocked(-1, 0)) {
				return false;
			}
			avatar.moveWest();
		}
		if (keycode == Input.Keys.RIGHT){
            avatar.setDirection(1);
			if (isMovementBlocked(1, 0)) {
				return false;
			}
            avatar.moveEast();
		}
		if (keycode == Input.Keys.UP){
            avatar.setDirection(3);
			if (isMovementBlocked(0, 1)) {
				return false;
			}
            avatar.moveNorth();
		}
		if (keycode == Input.Keys.DOWN){
			avatar.setDirection(0);
			if (isMovementBlocked(0, -1)) {
				return false;
			}
            avatar.moveSouth();
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		camera.translate(-deltaX, -deltaY);
		camera.update();
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub

	}
}
