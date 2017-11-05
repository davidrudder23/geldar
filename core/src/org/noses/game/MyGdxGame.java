package org.noses.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.noses.game.character.MovingCharacter;
import org.noses.game.path.Point;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame extends ApplicationAdapter implements ApplicationListener, GestureListener, InputProcessor {
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	private OrthographicCamera camera;

	private int tilePixelWidth;
	private int tilePixelHeight;

	MovingCharacter avatar;
	
	Map<String,Cell> highlights;

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

		avatar = new MovingCharacter("avatar.png", getObstructionLayers(), getAvatarLayer());
		
		highlights = new HashMap<>();
		
	    TiledMapTileLayer.Cell red = new TiledMapTileLayer.Cell();
	    red.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("red.png"))));
		highlights.put("red", red);
		
	    TiledMapTileLayer.Cell green = new TiledMapTileLayer.Cell();
	    green.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("green.png"))));
		highlights.put("green", green);
		
	    TiledMapTileLayer.Cell yellow = new TiledMapTileLayer.Cell();
	    yellow.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("yellow.png"))));
		highlights.put("yellow", yellow);
		
//		System.out.println("-----****-----");
//		System.out.println(avatar.getPath(new Point(90,90)));
//		System.out.println("-----****-----");
//		System.exit(0);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
			    getAvatarLayer().setCell(x,y, null);
			    getHighlightLayer().setCell(x,y, null);
			}
		}
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Highlight the mouse
		int tileX = Gdx.input.getX()/tilePixelWidth;
		int tileY = (int)((camera.viewportHeight-Gdx.input.getY())/tilePixelHeight);

//		List<Point> path = avatar.getPath(new Point(tileX, tileY));
//		if (path == null) {
//			getHighlightLayer().setCell(tileX, tileY, highlights.get("red"));			
//		} else {
//			for (Point point: path) {
//				getHighlightLayer().setCell(point.getX(), point.getY(), highlights.get("green"));			
//			}
//		}
//		
		recenterCamera();
		camera.update();

		
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
		
		//System.out.println("camHeightInTiles="+camHeightInTiles);
		//System.out.println("camWidthInTiles="+camWidthInTiles);
		//System.out.println("avatar.getX()="+avatar.getX());
		//System.out.println("avatar.getY()="+avatar.getY());

		if (avatar.getX() < camWidthInTiles) camCenterX = camWidthInTiles;
		if (avatar.getY() < camHeightInTiles) camCenterY = camHeightInTiles;

		float rightBoundary = getAvatarLayer().getWidth()-camWidthInTiles;
		if (avatar.getX() > rightBoundary) camCenterX = rightBoundary;

		float bottomBoundary = getAvatarLayer().getHeight()-camHeightInTiles;
		if (avatar.getY() > bottomBoundary) camCenterY = bottomBoundary;

		//System.out.println("camCenterX="+camCenterX);
		//System.out.println("camCenterY="+camCenterY);

		camera.position.set(camCenterX*tilePixelWidth, camCenterY*tilePixelHeight, 0);

	}

	private List<TiledMapTileLayer> getObstructionLayers() {
		return getLayersByName("obstruction");
	}
	
	private TiledMapTileLayer getHighlightLayer() {
		return getLayersByName("highlight").get(0);
	}
	
	private TiledMapTileLayer getAvatarLayer() {
		List<TiledMapTileLayer> avatarLayers = getLayersByName("walking");
		return avatarLayers.get(0);
	}

	private List<TiledMapTileLayer> getLayersByName(String name) {
		List<TiledMapTileLayer> obstructionLayers = new ArrayList<>();
		MapLayers mapLayers = tiledMap.getLayers();
		for (int i = 0; i < mapLayers.getCount(); i++) {
			MapLayer mapLayer = mapLayers.get(i);
			if ((mapLayer.getName().toLowerCase().contains(name.toLowerCase())) && (mapLayer instanceof TiledMapTileLayer)) {
				TiledMapTileLayer tmtl = (TiledMapTileLayer) mapLayer;
				obstructionLayers.add(tmtl);
			}
		}

		return obstructionLayers;
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.LEFT) {
            avatar.setDirection(2);
			if (avatar.isMovementBlocked(new Point(avatar.getX()-1, avatar.getY()))) {
				return false;
			}
			avatar.moveWest();
		}
		if (keycode == Input.Keys.RIGHT){
            avatar.setDirection(1);
			if (avatar.isMovementBlocked(new Point(avatar.getX()+1, avatar.getY()))) {
				return false;
			}
            avatar.moveEast();
		}
		if (keycode == Input.Keys.UP){
            avatar.setDirection(3);
			if (avatar.isMovementBlocked(new Point(avatar.getX(), avatar.getY()+1))) {
				return false;
			}
            avatar.moveNorth();
		}
		if (keycode == Input.Keys.DOWN){
			avatar.setDirection(0);
			if (avatar.isMovementBlocked(new Point(avatar.getX(), avatar.getY()-1))) {
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
		Point point = new Point(getTileXFromScreenX(screenX), getTileYFromScreenY(screenY));
		System.out.println("touchUp - "+point);
		avatar.moveTo(point);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		int tileX = screenX/tilePixelWidth;
		int tileY = (int)((camera.viewportHeight-screenY)/tilePixelHeight);

		//System.out.println("tileX="+tileX+", tileY="+tileY);
		getHighlightLayer().setCell(tileX, tileY, highlights.get("red"));
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
		System.out.println("tap");
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
	
	public int getTileXFromScreenX(int screenX) {
		int tileX = screenX/tilePixelWidth;
		//float camHeightInTiles = (camera.viewportHeight/2)/tilePixelHeight;
		return tileX;
	}
	
	public int getTileYFromScreenY(int screenY) {
		int tileY = (int)(camera.viewportHeight - screenY)/tilePixelHeight;
		//float camHeightInTiles = (camera.viewportHeight/2)/tilePixelHeight;
		return tileY;
	}

}
