package org.noses.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HUD {
	private Stage stage;
	private Table table;
	
	int score = 0;
	
	Label scoreLabel;
	Label timeLabel;
	
	boolean debug;

	public HUD () {
		stage = new Stage();
		
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		debug = false;

		// Add widgets to the table here.
		scoreLabel = new Label("1", new Skin(Gdx.files.internal("skin/rainbow-ui.json")));
		timeLabel = new Label("60", new Skin(Gdx.files.internal("skin/rainbow-ui.json")));
		table.add(scoreLabel).expand().top().left();
		table.add(timeLabel).expand().top().right();
	}
	
	public void toggleDebug() {
		debug = !debug;
		table.setDebug(debug); 
	}
	
	public void render() {
		scoreLabel.setText(score+"");
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void dispose() {
		stage.dispose();
	}

}
