package org.noses.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.noses.game.character.Avatar;
import org.noses.game.character.Dragon;
import org.noses.game.character.Mage;
import org.noses.game.character.MovingCharacter;
import org.noses.game.item.Item;
import org.noses.game.item.Vent;
import org.noses.game.path.MovingCollision;
import org.noses.game.path.Point;
import org.noses.game.ui.highscore.HighScoreListUI;
import org.noses.game.ui.highscore.HighScoreRepository;
import org.noses.game.ui.highscore.HighScoreUI;
import org.noses.game.ui.hud.HUD;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.Timer;

public class GeldarGame extends ApplicationAdapter implements ApplicationListener, GestureListener, InputProcessor {
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	private OrthographicCamera camera;

	private int tilePixelWidth;
	private int tilePixelHeight;

	TiledMapTileLayer avatarLayer;

	Avatar avatar;
	List<MovingCharacter> movingCharacters;
	List<Item> items;

	Map<String, Cell> highlights;

	HUD hud;
	HighScoreUI highScoreUI;
	HighScoreListUI highScoreListUI;

	int magnification;

	int timer;

	Sound gameOverSound;

	Timer.Task gameTimer;
	Timer.Task keyPressTimer;

	private Properties properties;

	@Override
	public void create() {

		properties = new Properties();
		try {
			properties.load(Gdx.files.internal("game.properties").read());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		magnification = 0;

		gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gameover.wav"));

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		hud = HUD.getInstance();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();
		tiledMap = new TmxMapLoader().load("game.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		MapProperties prop = tiledMap.getProperties();
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);

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

		HighScoreRepository.getInstance(getMongoPassword());

		startGame();
	}

	public void startGame() {

		items = new ArrayList<>();
		for (int x = 0; x < 100; x+=5) {
			for (int y = 0; y < 100; y+=5) {
				items.add(new Vent(this, new Point(x,y)));
			}
		}

		avatar = new Avatar(this);

		avatar.initialize();

		movingCharacters = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			movingCharacters.add(new Dragon(this));
		}

		for (int i = 0; i < 50; i++) {
			movingCharacters.add(new Mage(avatar, this));
		}

		MovingCollision.getInstance().setAvatar(avatar);
		MovingCollision.getInstance().setMovingCharacters(movingCharacters);

		MovingCollision.getInstance().setItems(items);

		Gdx.input.setInputProcessor(this);

		keyPressLoop();

		startTimer();
	}

	public void stopGame() {
		avatar.disable();
		// gameOverSound.play();

		for (MovingCharacter movingCharacter : movingCharacters) {
			movingCharacter.stop();
		}

		movingCharacters = new ArrayList<>();

		if (gameTimer != null) {
			gameTimer.cancel();
		}

	}

	public void restartGame() {
		System.exit(0);
	}

	private void keyPressLoop() {
		keyPressTimer = Timer.schedule(new Timer.Task() {

			@Override
			public void run() {

				if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.A))) {
					avatar.moveWest();
				}
				if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) || (Gdx.input.isKeyPressed(Input.Keys.D))) {
					avatar.moveEast();
				}
				if ((Gdx.input.isKeyPressed(Input.Keys.UP)) || (Gdx.input.isKeyPressed(Input.Keys.W))) {
					avatar.moveNorth();
				}
				if ((Gdx.input.isKeyPressed(Input.Keys.DOWN)) || (Gdx.input.isKeyPressed(Input.Keys.S))) {
					avatar.moveSouth();
				}

			}
		}, 1, 1 / avatar.getNumPerSecond());
	}

	private void startTimer() {
		timer = 10;
		hud.setTime(timer);

		GeldarGame self = this;

		gameTimer = Timer.schedule(new Timer.Task() {

			@Override
			public void run() {
				timer--;
				hud.setTime(timer);
				if (timer <= 0) {
					stopGame();

					highScoreUI = new HighScoreUI(self, getUILayer());
				}
			}
		}, 2f, 1);

	}

	@Override
	public void render() {
		for (int x = 0; x < getAvatarLayer().getWidth(); x++) {
			for (int y = 0; y < getAvatarLayer().getHeight(); y++) {
				getAvatarLayer().setCell(x, y, null);
			}
		}
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (items != null) {
			for (Item item : items) {
				item.render(getUILayer());
			}
		}

		// Highlight the mouse
		highlightTheMouse();

		recenterCamera();
		camera.update();

		// draw avatar
		TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
		cell.setTile(new StaticTiledMapTile(avatar.getAnimation()[avatar.getDirection()][avatar.getFrame()]));
		TiledMapTileLayer avatarLayer = getAvatarLayer();
		avatarLayer.setCell(avatar.getX(), avatar.getY(), cell);

		// draw dragons
		for (MovingCharacter movingCharacter : movingCharacters) {
			cell = new TiledMapTileLayer.Cell();
			cell.setTile(new StaticTiledMapTile(movingCharacter.getAnimation()[0][avatar.getFrame()]));
			TiledMapTileLayer dragonLayer = getAvatarLayer();
			dragonLayer.setCell(movingCharacter.getX(), movingCharacter.getY(), cell);
		}

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		hud.render();

		if (highScoreUI != null) {
			highScoreUI.render();
		}

		if (highScoreListUI != null) {
			highScoreListUI.render();
		}

	}

	private void highlightTheMouse() {
		if (Gdx.input.isButtonPressed(0)) {
			TiledMapTileLayer highlightLayer = getHighlightLayer();

			for (int x = 0; x < highlightLayer.getWidth(); x++) {
				for (int y = 0; y < highlightLayer.getWidth(); y++) {
					highlightLayer.setCell(x, y, null);
				}
			}
			int tileX = getTileXFromScreenX(Gdx.input.getX());
			int tileY = getTileYFromScreenY(Gdx.input.getY());

			List<Point> path = avatar.getPath(new Point(tileX, tileY));
			if (path == null) {
				highlightLayer.setCell(tileX, tileY, highlights.get("red"));
			} else {
				for (Point point : path) {
					highlightLayer.setCell(point.getX(), point.getY(), highlights.get("green"));
				}
			}
		}
	}

	private void recenterCamera() {
		float camCenterX = avatar.getX();
		float camCenterY = avatar.getY();

		float camHeightInTiles = (camera.viewportHeight / 2) / tilePixelHeight;
		float camWidthInTiles = (camera.viewportWidth / 2) / tilePixelWidth;

		if (avatar.getX() < camWidthInTiles)
			camCenterX = camWidthInTiles;
		if (avatar.getY() < camHeightInTiles)
			camCenterY = camHeightInTiles;

		float rightBoundary = getAvatarLayer().getWidth() - camWidthInTiles;
		if (avatar.getX() > rightBoundary)
			camCenterX = rightBoundary;

		float bottomBoundary = getAvatarLayer().getHeight() - camHeightInTiles;
		if (avatar.getY() > bottomBoundary)
			camCenterY = bottomBoundary;

		camera.position.set(camCenterX * tilePixelWidth, camCenterY * tilePixelHeight, 0);

	}

	public List<TiledMapTileLayer> getObstructionLayers() {
		return getLayersByName("obstruction");
	}

	public TiledMapTileLayer getHighlightLayer() {
		return getLayersByName("highlight").get(0);
	}

	public TiledMapTileLayer getUILayer() {
		return getLayersByName("ui").get(0);
	}

	public TiledMapTileLayer getAvatarLayer() {
		if (avatarLayer == null) {
			List<TiledMapTileLayer> avatarLayers = getLayersByName("walking");
			avatarLayer = avatarLayers.get(0);
		}
		return avatarLayer;
	}

	public List<Item> getItems() {
		return items;
	}

	private List<TiledMapTileLayer> getLayersByName(String name) {
		List<TiledMapTileLayer> obstructionLayers = new ArrayList<>();
		MapLayers mapLayers = tiledMap.getLayers();
		for (int i = 0; i < mapLayers.getCount(); i++) {
			MapLayer mapLayer = mapLayers.get(i);
			if ((mapLayer.getName().toLowerCase().contains(name.toLowerCase()))
					&& (mapLayer instanceof TiledMapTileLayer)) {
				TiledMapTileLayer tmtl = (TiledMapTileLayer) mapLayer;
				obstructionLayers.add(tmtl);
			}
		}

		return obstructionLayers;
	}

	@Override
	public void resize(int width, int height) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		if (magnification > 0) {
			w = (int) ((w / 2) / magnification + (w / 2));
			h = (int) ((h / 2) / magnification + (h / 2));
		}
		camera.setToOrtho(false, w, h);
		camera.update();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		avatar.stop();
		if (keycode == Input.Keys.T) {
			hud.toggleDebug();
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
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				getHighlightLayer().setCell(x, y, null);
			}
		}

		getHighlightLayer().setCell(getTileXFromScreenX(screenX), getTileYFromScreenY(screenY), highlights.get("red"));
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		magnification += amount;

		resize(0, 0);

		return false;
	}

	@Override
	public void dispose() {
		hud.dispose();
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

		// System.out.println("Viewport=" + camera.viewportWidth + "," +
		// camera.viewportHeight);
		int camWidthInTiles = (int) (camera.viewportWidth / tilePixelWidth);

		int tileX = screenX / tilePixelWidth;

		int offset = avatar.getX() - (camWidthInTiles / 2);
		if (offset > 0) {
			tileX += offset;
		}

		return tileX;
	}

	public int getTileYFromScreenY(int screenY) {
		int camHeightInTiles = (int) (camera.viewportHeight / tilePixelHeight);
		int tileY = (int) (camera.viewportHeight - screenY) / tilePixelHeight;

		int offset = avatar.getY() - (camHeightInTiles / 2);
		if (offset > 0) {
			tileY += offset;
		}
		return tileY;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public String getProperty(String name) {
		return properties.getProperty(name);
	}

	public String getMongoPassword() {
		return getProperty("mongo.password");
	}

	public void displayHighScoreListUI(String name) {
		highScoreUI = null;

		highScoreListUI = new HighScoreListUI(this, name);
		highScoreListUI.display(getUILayer());
	}

}
