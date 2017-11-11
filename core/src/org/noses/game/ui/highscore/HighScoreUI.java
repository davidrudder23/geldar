package org.noses.game.ui.highscore;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HighScoreUI {
	private Stage stage;
	private Table table;

	private MyGdxGame parent;

	public HighScoreUI(MyGdxGame parent, TiledMapTileLayer layer) {
		this.parent = parent;

		stage = new Stage();

		table = new Table();
		table.setFillParent(true);
		table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("fog.png"))));

		stage.addActor(table);

		// table.setDebug(true);

		Skin skin = new Skin(Gdx.files.internal("skin/rainbow-ui.json"));
		TextField input = new TextField("", skin);
		Button button = new Button(skin);
		button.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (button.isChecked()) {
					if ((input.getText() == null) || (input.getText().equals(""))) {
						return;
					}
					HighScoreRepository.getInstance().saveScore(input.getText(), parent.getAvatar().getScore());
					stage = new Stage();
					Gdx.input.setInputProcessor(parent);

					parent.displayHighScoreListUI();
				}
			}
		});

		// Add widgets to the table here.
		table.add(new Label("Enter your name for the high score board", skin));
		table.row();

		Table inputTable = new Table();
		inputTable.add(input);
		inputTable.add(button);
		table.add(inputTable);

		Gdx.input.setInputProcessor(stage);
	}

	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
}
