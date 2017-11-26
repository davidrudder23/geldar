package org.noses.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

	Skin skin;

	private HUD (GeldarGame parent) {

		this.parent = parent;

		stage = new Stage();
		
		table = new Table();
		//table.setDebug(true);
		table.setFillParent(true);
		stage.addActor(table);
		
		debug = false;

		/*
		skin/arcade/skin/arcade-ui.json
skin/biological-attack/skin/biological-attack-ui.json
skin/clean-crispy/skin/clean-crispy-ui.json
skin/cloud-form/skin/cloud-form-ui.json
skin/comic/skin/comic-ui.json
skin/commodore64/skin/uiskin.json
skin/craftacular/skin/craftacular-ui.json
skin/default/skin/uiskin.json
skin/expee/skin/expee-ui.json
skin/flat-earth/skin/flat-earth-ui.json
skin/flat/skin/skin.json
skin/freezing/skin/freezing-ui.json
skin/gdx-holo/skin/uiskin.json
skin/glassy/skin/glassy-ui.json
skin/golden-spiral/skin/golden-ui-skin.json
skin/kenney-pixel/skin/skin.json
skin/level-plane/skin/level-plane-ui.json
skin/lgdxs/skin/lgdxs-ui.json
skin/lml/skin/skin.json
skin/metal/skin/metal-ui.json
skin/neon/skin/neon-ui.json
skin/neutralizer/skin/neutralizer-ui.json
skin/number-cruncher/skin/number-cruncher-ui.json
skin/orange/skin/uiskin.json
skin/pixthulhu/skin/pixthulhu-ui.json
skin/plain-james/skin/plain-james-ui.json
skin/quantum-horizon/skin/quantum-horizon-ui.json
skin/rainbow/skin/rainbow-ui.json
skin/rusty-robot/skin/rusty-robot-ui.json
skin/sgx/skin/sgx-ui.json
skin/shade/skin/uiskin.json
skin/skin-composer/skin/skin-composer-ui.json
skin/star-soldier/skin/star-soldier-ui.json
skin/terra-mother/skin/terra-mother-ui.json
skin/tracer/skin/tracer-ui.json
skin/tubular/skin/tubular-ui.json
skin/vhs/skin/vhs-ui.json
		 */

		skin = new Skin(Gdx.files.internal("skin/commodore64/skin/uiskin.json"));

		// Add widgets to the table here.
		scoreLabel = new Label("1", skin);
		table.add(scoreLabel).pad(10).fillX().align(Align.left);

		timeLabel = new Label("60", skin);
		table.add(timeLabel).pad(10).fillX().align(Align.right);

		table.row();
		inventoryTable = new Table();

		//inventoryTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("fog.png"))));

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
			Image itemIcon = new Image(sortedInventory.get(inventoryType).get(0).getTextureRegion());
			inventoryTable.add(itemIcon).pad(10).left();

			Label label = new Label(sortedInventory.get(inventoryType).size()+"", skin);
			label.setFontScale(2.0f);

			inventoryTable.add(label)
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
