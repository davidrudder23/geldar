package org.noses.game.character;

import java.util.List;

import org.noses.game.GeldarGame;
import org.noses.game.item.Item;
import org.noses.game.path.Point;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Dragon extends MovingCharacter {

	public Dragon(GeldarGame parent) {
		super("dragon.png", parent);

		chooseNextSpot();
	}

	@Override
	public void chooseNextSpot() {
		Point point = null;
		do {
			int x = (int) (Math.random() * parent.getAvatarLayer().getWidth());
			int y = (int) (Math.random() * parent.getAvatarLayer().getHeight());

			point = new Point(x, y);
		} while (isMovementBlocked(point));

		moveTo(point);
	}

	@Override
	protected float getNumPerSecond() {
		return 5;
	}

	@Override
	public void collideWith(MovingCharacter collider) {
	}

	@Override
	public void collideWith(Item item) {
		// don't do anything right now
	}

}
