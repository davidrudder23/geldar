package org.noses.game.item;

import com.badlogic.gdx.utils.Timer;
import org.noses.game.GeldarGame;
import org.noses.game.path.Point;

public class SilverStar extends Item {
    public static final int NEW = 0;
    public static final int CRACKED = 1;
    public static final int HATCHED = 2;

    Timer.Task eggTimer;
    GeldarGame parent;

    public SilverStar(GeldarGame parent) {
        super(parent, "silver_star.png", new Point());

    }

    public String getItemName() {
        return "silver star";
    }

    @Override
    public int getNumPerSecond() {
        return 4;
    }

    @Override
    public boolean isObstruction() {
        return false;
    }

    public boolean isInventory() {
        return true;
    }

    public boolean touchDown(int button) {
        return true;
    }

    public float getNumFramesPerSecond() {
        return 5;
    }

}
