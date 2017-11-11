package org.noses.game.ui.highscore;

import org.noses.game.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class HighScoreUI {
	private Stage stage;
	private Table table;
	
	private MyGdxGame parent;
	
	public HighScoreUI(MyGdxGame parent) {
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
		stage.addActor(table);
		
		table.setDebug(true);
		
		Skin skin = new Skin(Gdx.files.internal("skin/rainbow-ui.json"));
		TextField input = new TextField("Name", skin);
		Button button = new Button(skin);
		button.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (button.isChecked()) {
                    new HighScoreRepository(parent.getProperty("mongo.password")).saveScore(input.getText(), parent.getAvatar().getScore());
				}
				
				stage = new Stage();
				Gdx.input.setInputProcessor(parent);
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
	
}
