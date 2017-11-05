package org.noses.game.character;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.List;

public class Dragon extends MovingCharacter {

    public Dragon(List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
        super("dragon.png", obstructionLayers, avatarLayer);
    }

    private void chooseNextSpot() {

    }
    
}
