package org.noses.game.character;

import java.util.List;

import org.noses.game.GeldarGame;
import org.noses.game.item.Item;
import org.noses.game.path.Point;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Mage extends MovingCharacter {

	Avatar avatar;

	public Mage(Avatar avatar, GeldarGame parent) {
		super("mage.png", parent);

		this.avatar = avatar;
		chooseNextSpot();
	}

	@Override
	public void chooseNextSpot() {
		Point point = null;

		int randomInt = (int) (Math.random() * 100);

		if (randomInt < 15) {

			point = new Point(avatar.getX(), avatar.getY());
		} else {
			do {
				int x = (int) (Math.random() * parent.getAvatarLayer().getWidth());
				int y = (int) (Math.random() * parent.getAvatarLayer().getHeight());

				point = new Point(x, y);
			} while (isMovementBlocked(point));
		}

		moveTo(point);
	}

	@Override
	protected float getNumPerSecond() {
		return 3;
	}

	@Override
	public void collideWith(MovingCharacter collider) {
		System.out.println("Mage collided");
	}

	@Override
	public void collideWith(Item item) {
		// don't do anything right now
	}

}
