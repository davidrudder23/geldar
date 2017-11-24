package org.noses.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.noses.game.GeldarGame;
import org.noses.game.path.Point;

import java.util.List;

public class Vent extends Item {
    private Egg egg;

    public Vent(GeldarGame parent) {
        this(parent, new Point(0,0));
    }

    public Vent(GeldarGame parent, Point origin) {
        Texture avatarAnimationSheet = new Texture("vent.png");
        TextureRegion[][] regions = TextureRegion.split(avatarAnimationSheet, (int) parent.getAvatarLayer().getTileWidth(), (int) parent.getAvatarLayer().getTileWidth());

        if (regions.length <= 0) {
            System.out.println("Error: vent pixmap must be at least 1 tile large");
        }
        tiles = new Tile[regions.length][regions[0].length];

        for (int x = 0; x < regions.length; x++) {
            for (int y = 0; y < regions[x].length; y++) {
                TextureRegion[] region = new TextureRegion[1];
                region[0] = regions[x][y];
                Tile tile = new Tile(new Point(y, 2 - x), region);
                tiles[x][y] = tile;
            }
        }

        this.point = origin;
        startAnimation();
    }

    public String getItemName() {
        return "vent";
    }

    @Override
    public boolean isObstruction() {
        return true;
    }

    public boolean isInventory() {
        return false;
    }


    public int getNumPerSecond() {
        return 5;
    }


    @Override
    public boolean touchUp(int button) {
        System.out.println("Vent touch up");
        return super.touchUp(button);
    }

    public void addEgg(Egg egg) {
        this.egg = egg;
    }
}
