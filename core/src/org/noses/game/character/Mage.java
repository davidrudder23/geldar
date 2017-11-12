package org.noses.game.character;

import java.util.List;

import org.noses.game.path.Point;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Mage extends MovingCharacter {

	Avatar avatar;

	public Mage(Avatar avatar, List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
		super("mage.png", obstructionLayers, avatarLayer);

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
				int x = (int) (Math.random() * avatarLayer.getWidth());
				int y = (int) (Math.random() * avatarLayer.getHeight());

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
}
