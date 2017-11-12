package org.noses.game.character;

import java.util.List;

import org.noses.game.path.Point;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Dragon extends MovingCharacter {

	public Dragon(List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
		super("dragon.png", obstructionLayers, avatarLayer);

		chooseNextSpot();
	}

	@Override
	public void chooseNextSpot() {
		Point point = null;
		do {
			int x = (int) (Math.random() * avatarLayer.getWidth());
			int y = (int) (Math.random() * avatarLayer.getHeight());

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
}
