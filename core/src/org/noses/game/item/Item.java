package org.noses.game.item;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Item {

    Tile[][] tiles;

    public void render(TiledMapTileLayer layer) {
        if (!canRender(layer)) {
            System.out.println("Can't render "+this);
            return;
        }
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                Tile tile  = tiles[x][y];
                if (tile != null) {
                    tile.render(layer);
                }
            }
        }
    }

    public boolean canRender(TiledMapTileLayer layer) {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                Tile tile  = tiles[x][y];
                if (tile != null) {
                    if (!tile.canRender(layer)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public String toString() {
        return tiles.length+"x"+tiles[0].length+" tile";
    }
}
