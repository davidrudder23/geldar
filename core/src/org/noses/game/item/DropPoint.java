package org.noses.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import org.noses.game.GeldarGame;
import org.noses.game.character.inventory.Inventory;
import org.noses.game.path.Point;

import java.util.HashMap;
import java.util.List;

public class DropPoint extends Item {
    private Timer.Task dropoffTask;

    private boolean canDropoff = true;

    private Sound dropoffSound;

    public DropPoint(GeldarGame parent) {
        this(parent, new Point(0, 0));
    }

    public DropPoint(GeldarGame parent, Point point) {
        super(parent, "droppoint.png", point);

        canDropoff = true;

        dropoffSound = Gdx.audio.newSound(Gdx.files.internal("sounds/dropoff.wav"));
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

    public void pauseDropoff() {
        canDropoff = false;

        dropoffSound.play();

        if ((dropoffTask != null) && (dropoffTask.isScheduled())) {
            dropoffTask.cancel();
        }
        dropoffTask =
                Timer.instance().scheduleTask(new Timer.Task() {

                    @Override
                    public void run() {
                        canDropoff = true;
                        dropoffTask.cancel();
                    }
                }, 0.5f);

    }


    @Override
    public boolean touchUp(int button) {
        System.out.println("DropPoint touch up");
        return super.touchUp(button);
    }

    public float getNumFramesPerSecond() {
        return 5;
    }

    public void avatarIsNear() {
        if (!canDropoff) {
            return;
        }
        Inventory inventory = parent.getAvatar().getInventory();
        HashMap<String, List<Item>> sortedItems = inventory.getSortedInventory();


        List<Item> bronzeStars = sortedItems.get("bronze star");
        if ((bronzeStars != null) && bronzeStars.size() > 0) {
            if (inventory.remove(bronzeStars.get(0))) {
                parent.getAvatar().setScore(parent.getAvatar().getScore() + 1);
                pauseDropoff();
                return;
            }
        }

        List<Item> silverStars = sortedItems.get("silver star");
        if ((silverStars != null) && silverStars.size() > 0) {
            if (inventory.remove(silverStars.get(0))) {
                parent.getAvatar().setScore(parent.getAvatar().getScore() + 2);
                pauseDropoff();
                return;
            }
        }

        List<Item> goldStars = sortedItems.get("gold star");
        if ((goldStars != null) && goldStars.size() > 0) {
            if (inventory.remove(goldStars.get(0))) {
                parent.getAvatar().setScore(parent.getAvatar().getScore() + 3);
                pauseDropoff();
                return;
            }
        }

        return;
    }

}
