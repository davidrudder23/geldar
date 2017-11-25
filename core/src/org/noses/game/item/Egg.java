package org.noses.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import org.noses.game.GeldarGame;
import org.noses.game.path.Point;

public class Egg extends Item {
    public static final int NEW = 0;
    public static final int CRACKED = 1;
    public static final int HATCHED = 2;

    private int state = 0;

    Timer.Task eggTimer;
    GeldarGame parent;

    public Egg(GeldarGame parent, String spriteFilename) {
        super(parent, "egg.png", new Point());

    }

    public String getItemName() {
        return "egg";
    }

    public int getState() {
        return state;
    }

    public void nextState() {
        if (state < 2) {
            state++;
        }
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
