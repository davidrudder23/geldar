package org.noses.game.character;

import java.util.List;

import org.noses.game.path.MovingCollision;
import org.noses.game.ui.hud.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Avatar extends MovingCharacter {

	private boolean canCapture;
	private boolean canBeHurt;

	private Task captureTask;
	private Task hurtTask;

	private int score;

	Sound captureSound;
	Sound walkSound;
	Sound hurtSound;

	public Avatar(List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
		super("avatar.png", obstructionLayers, avatarLayer);

		captureSound = Gdx.audio.newSound(Gdx.files.internal("sounds/inventory/coin.wav"));
		hurtSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt.wav"));
		walkSound = Gdx.audio.newSound(Gdx.files.internal("sounds/walk.wav"));

		initialize();
	}

	public void initialize() {
		canCapture = true;
		canBeHurt = true;

		findAGoodSpot();

		score = 0;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	protected void walk() {
		walkSound.play(0.5f);
		super.walk();
	}

	@Override
	public float getNumPerSecond() {
		return 7;
	}

	public boolean canCapture() {
		return canCapture;
	}

	public void captured(MovingCharacter collider) {
		if (!canCapture) {
			return;
		}

		MovingCollision.getInstance().getMovingCharacters().remove(collider);
		score++;

		HUD.getInstance().setScore(score);

		captureSound.play();

		/*
		 * canCapture = false;
		 * 
		 * if ((captureTask != null) && (captureTask.isScheduled())) {
		 * captureTask.cancel(); } captureTask =
		 * Timer.instance().scheduleTask(new Task() {
		 * 
		 * @Override public void run() { canCapture = true;
		 * captureTask.cancel(); } }, 1);
		 */
	}

	public boolean canBeHurt() {
		return canBeHurt;
	}

	public void hurt() {
		if (!canBeHurt()) {
			return;
		}
		score -= 2;
		canBeHurt = false;

		if ((hurtTask != null) && (hurtTask.isScheduled())) {
			hurtTask.cancel();
		}
		hurtTask = Timer.instance().scheduleTask(new Task() {

			@Override
			public void run() {
				canBeHurt = true;
				hurtTask.cancel();
			}
		}, 2);

		HUD.getInstance().setScore(score);

		hurtSound.play();
	}

	@Override
	public void collideWith(MovingCharacter collider) {
		if (collider instanceof Dragon) {
			captured(collider);
		} else if (collider instanceof Mage) {
			hurt();
		}
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
		stop();
	}

	public int getScore() {
		return score;
	}

}
