package org.noses.game.item;

import com.badlogic.gdx.utils.Timer;
import org.noses.game.GeldarGame;
import org.noses.game.path.Point;

public class GoldStar extends BaseStar {
    public static final int NEW = 0;
    public static final int CRACKED = 1;
    public static final int HATCHED = 2;

    Timer.Task eggTimer;
    GeldarGame parent;

    public GoldStar(GeldarGame parent) {
        super(parent, "gold_star.png", new Point());

    }

    @Override
    public float getRandomizationChange() {
        return 0.7f;
    }

    public String getItemName() {
        return "gold star";
    }

    @Override
    public int getNumPerSecond() {
        return 2;
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
