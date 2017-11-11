package org.noses.game.ui.highscore;

import java.util.List;

import org.noses.game.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HighScoreListUI {
	private Stage stage;
	private Table table;

	private MyGdxGame parent;

	public HighScoreListUI(MyGdxGame parent) {
		this.parent = parent;
	}

	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void display(TiledMapTileLayer layer) {
		stage = new Stage();

		table = new Table();
		table.setFillParent(true);
		table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("fog.png"))));
		stage.addActor(table);

		// table.setDebug(true);

		Skin skin = new Skin(Gdx.files.internal("skin/rainbow-ui.json"));
		Label ok = new Label("Okay", skin);
		Button button = new Button(ok, skin);
		button.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (button.isChecked()) {
					stage = new Stage();
					Gdx.input.setInputProcessor(parent);
					parent.startGame();
				}
			}
		});

		List<HighScore> highScores = HighScoreRepository.getInstance().getHighScores();

		highScores.stream().forEach(hs -> {
			Label name = new Label(hs.getName(), skin);
			Label score = new Label(hs.getScore() + "", skin);
			table.add(name);
			table.add(score);
			table.row();
		});

		table.add(button);

		Gdx.input.setInputProcessor(stage);
	}

}
