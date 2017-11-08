package org.noses.game.character;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import org.noses.game.hud.HUD;
import org.noses.game.path.MovingCollision;

public class Avatar extends MovingCharacter {

	private boolean canCapture;
	private boolean canBeHurt;

	private Task captureTask;
	private Task hurtTask;

	private int score;

	Sound captureSound;
	Sound walkSound;

	public Avatar(List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
		super("avatar.png", obstructionLayers, avatarLayer);

		canCapture = true;
		canBeHurt = true;
		x = 0;
		y = 0;

		score = 0;

        captureSound = Gdx.audio.newSound(Gdx.files.internal("sounds/inventory/coin.wav"));
        walkSound = Gdx.audio.newSound(Gdx.files.internal("sounds/walk.wav"));

	}

	@Override
    protected void walk() {
	    walkSound.play(0.5f);
        super.walk();
    }

        @Override
	protected float getNumPerSecond() {
		return 7;
	}

	public boolean canCapture() {
		return canCapture;
	}

	public void captured() {
		if (!canCapture) {
			return;
		}
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
		}, 1, 1);
        
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

	public void collideWith(MovingCharacter collider) {
		if (!canCapture) {
			return;
		}

		if (collider instanceof Dragon) {
			MovingCollision.getInstance().getDragons().remove(collider);
			score++;

            HUD.getInstance().setScore(score);

			captureSound.play();
		}

		captured();
	}

    @Override
    public void setX(int x) {
	    walkSound.play(0.5f);
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        walkSound.play(0.5f);
        super.setY(y);
    }

    public void disable() {
	    canCapture = false;
	    canBeHurt = false;
    }

}
