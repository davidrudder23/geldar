package org.noses.game.character;

import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Avatar extends MovingCharacter {

	private boolean canCapture;
	private boolean canBeHurt;

	private Task captureTask;
	private Task hurtTask;

	private int score;

	public Avatar(List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
		super("avatar.png", obstructionLayers, avatarLayer);

		x = 0;
		y = 0;

		score = 0;
	}

	@Override
	protected float getNumPerSecond() {
		return 7;
	}

	public boolean canCapture() {
		return canCapture;
	}

	public void captured() {
		score++;
        canCapture = false;
        
        if ((captureTask != null) && (captureTask.isScheduled())) {
        	captureTask.cancel();
        }
        captureTask = Timer.schedule(new Task() {
			
			@Override
			public void run() {
				canCapture = true;
				captureTask.cancel();
			}
		}, 3, 0);
        
	}

	public boolean canBeHurt() {
		return canBeHurt;
	}

	public void hurt() {
		score -= 2;
		canBeHurt = false;
		hurtTask = Timer.schedule(new Task() {

			@Override
			public void run() {
				canBeHurt = true;
				hurtTask.cancel();
			}
		}, 3, 0);
	}

}
