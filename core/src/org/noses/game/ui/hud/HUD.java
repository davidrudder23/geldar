package org.noses.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import org.noses.game.GeldarGame;
import org.noses.game.item.Item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HUD {
	private Stage stage;
	private Table table;
	
	int score = 0;

	int time = 0;
	
	Label scoreLabel;
	Label timeLabel;

	Table inventoryTable;
	
	boolean debug;

	GeldarGame parent;

	private static HUD instance;

	private HUD (GeldarGame parent) {

		this.parent = parent;

		stage = new Stage();
		
		table = new Table();
		//table.setDebug(true);
		table.setFillParent(true);
		stage.addActor(table);
		
		debug = false;

		// Add widgets to the table here.
		scoreLabel = new Label("1", new Skin(Gdx.files.internal("skin/rainbow-ui.json")));
		table.add(scoreLabel).pad(10).fillX().align(Align.left);

		timeLabel = new Label("60", new Skin(Gdx.files.internal("skin/rainbow-ui.json")));
		table.add(timeLabel).pad(10).fillX().align(Align.right);

		table.row();
		inventoryTable = new Table();

		table.add(inventoryTable).expand().pad(10).fillY().align(Align.left);
		table.add().right();
	}

	public static HUD getInstance(GeldarGame parent) {
		if (instance == null) {
			instance = new HUD(parent);
		}
		return instance;
	}
	
	public void toggleDebug() {
		debug = !debug;
		table.setDebug(debug); 
	}

	public void buildInventory() {

		inventoryTable.clearChildren();

		HashMap<String, List<Item>> sortedInventory = parent.getAvatar().getInventory().getSortedInventory();

		for (String inventoryType: sortedInventory.keySet()) {
			inventoryTable.add(new Label(inventoryType, new Skin(Gdx.files.internal("skin/rainbow-ui.json"))))
					.pad(10)
					.right();
			inventoryTable.add(sortedInventory.get(inventoryType).get(0).getActor());
			inventoryTable.add(new Label(sortedInventory.get(inventoryType).size()+"", new Skin(Gdx.files.internal("skin/rainbow-ui.json"))))
					.pad(10)
					.left();
			inventoryTable.row();
		}

 	}
	
	public void render() {
		scoreLabel.setText("Score: "+score);
		timeLabel.setText("Time: "+time);

		buildInventory();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public void dispose() {
		stage.dispose();
	}

}
