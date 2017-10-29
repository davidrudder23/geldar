package org.noses.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MyGdxGame extends ApplicationAdapter implements ApplicationListener, GestureListener, InputProcessor {
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	private OrthographicCamera camera;

	private int tilePixelWidth;
	private int tilePixelHeight;

	TextureRegion[][] avatarAnimation;
	
	private int avatarLeft;
	private int avatarTop;
	private int avatarDirection;
	private int avatarCurrentFrame;

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

		avatarLeft = 0;
		avatarTop = tilePixelHeight;
		avatarDirection = 0;
		
		avatarAnimation = getAvatarAnimation();
		
		Timer.schedule(new Task(){
            @Override
            public void run() {
            	avatarCurrentFrame++;
                if(avatarCurrentFrame > 3)
                	avatarCurrentFrame = 0;
            }
        }
        ,0f,1/10.0f);

		// avatarSprite = new Sprite();
		Gdx.input.setInputProcessor(this);
	}

	private TextureRegion[][] getAvatarAnimation() {
		Texture walkSheet = new Texture("avatar.png");
		TextureRegion[][] frames = TextureRegion.split(walkSheet, walkSheet.getWidth() / 4, walkSheet.getHeight() / 4);

		return frames;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		recenterCamera();
		
	    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
	    cell.setTile(new StaticTiledMapTile(avatarAnimation[avatarDirection][avatarCurrentFrame]));
	    TiledMapTileLayer avatarLayer = getAvatarLayer();
	    avatarLayer.setCell(avatarLeft/tilePixelWidth, avatarTop/tilePixelHeight, cell);
	    
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
	}

	private void recenterCamera() {
		float camCenterX = avatarLeft;
		float camCenterY = avatarTop;

		float camTop = avatarTop - (camera.viewportHeight/2);

		if (avatarLeft < camera.viewportWidth/2) camCenterX = (camera.viewportWidth/2);
		if (avatarTop < (camera.viewportHeight/2)) camCenterY = (camera.viewportHeight/2);

		float rightBoundary = (getAvatarLayer().getWidth()*tilePixelWidth)-(camera.viewportWidth/2);
		if (avatarLeft > rightBoundary) camCenterX = rightBoundary;

		float bottomBoundary = (getAvatarLayer().getHeight()*tilePixelHeight)-(camera.viewportHeight/2);
		if (avatarTop > bottomBoundary) camCenterY = bottomBoundary;

		camera.position.set(camCenterX, camCenterY, 0);

		camera.update();
	}

	private boolean isCollision(int moveHoriz, int moveVert) {
		int left = (int) (Math.floor(avatarLeft / tilePixelWidth))+moveHoriz;
		int right = left + (int) (Math.floor(tilePixelWidth / tilePixelWidth));
		int top = (int) (Math.floor(avatarTop / tilePixelHeight))+moveVert;
		int bottom = top + (int) (Math.floor(tilePixelHeight / tilePixelHeight));

		List<TiledMapTileLayer> obstructionLayers = getObstructionLayers();

		for (TiledMapTileLayer ml: obstructionLayers) {
			for (int x = left; x < right; x++) {
				for (int y = top; y < bottom; y++) {
					if (ml.getCell(x, y) != null) {
						return true;
					}
				}
			}
		};
		return false;
	}

	private boolean isOffScreen(int moveHoriz, int moveVert) {
		int newTileX = (avatarLeft/tilePixelWidth) + moveHoriz;
		int newTileY = (avatarTop/tilePixelHeight) + moveVert;

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
	    avatarLayer.setCell(avatarLeft/tilePixelWidth, avatarTop/tilePixelHeight, null);

		if (keycode == Input.Keys.LEFT) {
			avatarDirection = 2;
			if (isMovementBlocked(-1, 0)) {
				return false;
			}
			avatarLeft -= 32;
		}
		if (keycode == Input.Keys.RIGHT){
			avatarDirection = 1;
			if (isMovementBlocked(1, 0)) {
				return false;
			}
			avatarLeft += 32;
		}
		if (keycode == Input.Keys.UP){
			avatarDirection = 3;
			if (isMovementBlocked(0, 1)) {
				return false;
			}
			avatarTop += 32;
		}
		if (keycode == Input.Keys.DOWN){
			avatarDirection = 0;
			if (isMovementBlocked(0, -1)) {
				return false;
			}
			avatarTop -= 32;
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		System.out.println("char="+character);
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
