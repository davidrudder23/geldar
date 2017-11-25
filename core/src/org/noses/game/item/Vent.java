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

    public Vent(GeldarGame parent, Point point) {
        super(parent, "vent.png", point);
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

    public float getNumFramesPerSecond() {
        return 5;
    }

}
