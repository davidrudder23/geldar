package org.noses.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import org.noses.game.GeldarGame;
import org.noses.game.character.Avatar;
import org.noses.game.path.Point;
import org.noses.game.ui.hud.HUD;

public class SpeedUp extends Item {

    Sound speedUpSound;

    private static Timer.Task speedUpTimer;

    public SpeedUp(GeldarGame parent, Point point) {
        super(parent, "speedup.png", point);

        speedUpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/speedup.wav"));
    }

        @Override
    public String getItemName() {
        return "speed up";
    }

    @Override
    public int getNumPerSecond() {
        return 2;
    }

    @Override
    public boolean isObstruction() {
        return false;
    }

    @Override
    public boolean isInventory() {
        return true;
    }

    @Override
    public float getNumFramesPerSecond() {
        return 0;
    }

    @Override
    public void avatarIsNear() {

    }

    @Override
    public void collideWith(Avatar avatar) {
        System.out.println("Speeding up avatar");
        speedUpSound.play();
        avatar.setNumPerSecond(10);
        parent.getItems().remove(this);

        if (speedUpTimer != null) {
            speedUpTimer.cancel();
        }
        speedUpTimer = Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                avatar.setNumPerSecond(7);
            }
        }, 5);
    }

}
