package org.noses.game.ui.highscore;

import java.util.List;

import org.noses.game.GeldarGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HighScoreListUI {
	private Stage stage;
	private Table table;

	private GeldarGame parent;
	private String playerName;

	public HighScoreListUI(GeldarGame parent, String playerName) {
		this.parent = parent;
		this.playerName = playerName;
	}

	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void display(TiledMapTileLayer layer) {
		Skin skin = new Skin(Gdx.files.internal("skin/rainbow-ui.json"));

		stage = new Stage();

		table = new Table();
		table.setFillParent(true);
		table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("fog.png"))));
		stage.addActor(table);

		Label title = new Label("High Scores", skin);
		title.setFontScale(2.5f);

		table.add(title).colspan(2);
		table.row();

		// table.setDebug(true);

		Label ok = new Label("Okay", skin);
		Button button = new Button(ok, skin);
		button.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (button.isChecked()) {
					stage = new Stage();
					Gdx.input.setInputProcessor(parent);
					parent.restartGame();
				}
			}
		});

		List<HighScore> highScores = HighScoreRepository.getInstance().getHighScores();

		highScores.stream().forEach(hs -> {
			Label name = new Label(hs.getName(), skin);
			Label score = new Label(hs.getScore() + "", skin);

			if ((hs.getName().equals(playerName)) && (hs.getScore() == parent.getAvatar().getScore())) {
				System.out.println("Coloring red");
				name.setStyle(new LabelStyle(name.getStyle().font, Color.GOLD));
				score.setStyle(new LabelStyle(score.getStyle().font, Color.GOLD));
			}

			table.add(name);
			table.add(score);
			table.row();
		});

		table.add(button);

		Gdx.input.setInputProcessor(stage);
	}

}
