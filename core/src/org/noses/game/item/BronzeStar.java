package org.noses.game.item;

import com.badlogic.gdx.utils.Timer;
import org.noses.game.GeldarGame;
import org.noses.game.path.Point;

public class BronzeStar extends BaseStar {
    public static final int NEW = 0;
    public static final int CRACKED = 1;
    public static final int HATCHED = 2;

    Timer.Task eggTimer;
    GeldarGame parent;

    public BronzeStar(GeldarGame parent) {
        super(parent, "bronze_star.png", new Point());

    }

    public String getItemName() {
        return "bronze star";
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
