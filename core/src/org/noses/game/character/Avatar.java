package org.noses.game.character;

import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Avatar extends MovingCharacter {

	public Avatar(List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
        super("avatar.png", obstructionLayers, avatarLayer);

        x=0;
        y=0;
	}
	
	@Override
	protected float getNumPerSecond() {
		return 7;
	}
}
