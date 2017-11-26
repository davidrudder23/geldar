package org.noses.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import org.noses.game.GeldarGame;
import org.noses.game.character.inventory.Inventory;
import org.noses.game.item.Item;
import org.noses.game.path.MovingCollision;
import org.noses.game.path.Point;
import org.noses.game.ui.hud.HUD;

import java.util.List;
import java.util.stream.Collectors;

public class Avatar extends MovingCharacter {

    private boolean canCapture;
    private boolean canBeHurt;

    private Task captureTask;
    private Task hurtTask;

    private int score;

    Sound captureSound;
    Sound pickupSound;
    Sound walkSound;
    Sound hurtSound;

    Inventory inventory;

    public Avatar(GeldarGame parent) {
        super("avatar.png", parent);

        captureSound = Gdx.audio.newSound(Gdx.files.internal("sounds/capture.wav"));
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("sounds/pickup.wav"));
        hurtSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt.wav"));
        walkSound = Gdx.audio.newSound(Gdx.files.internal("sounds/walk.wav"));

        initialize();

        inventory = new Inventory();
    }

    public void initialize() {
        canCapture = true;
        canBeHurt = true;

        findAGoodSpot();

        score = 0;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    protected void walk() {
        walkSound.play(0.5f);
        super.walk();
    }

    public List<Item> findNearbyItems( List<Item> items) {
        return items.stream()
                .filter(i -> {

                    if (i.getX() < getX() - 1) {
                        return false;
                    }
                    if (i.getX() > getX() + 1) {
                        return false;
                    }
                    if (i.getY() < getY() - 1) {
                        return false;
                    }
                    if (i.getY() > getY() + 1) {
                        return false;
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public float getNumPerSecond() {
        return 7;
    }

    public boolean canCapture() {
        return canCapture;
    }

    public void captured(MovingCharacter collider) {
        if (!canCapture) {
            return;
        }

        MovingCollision.getInstance().getMovingCharacters().remove(collider);
        score++;

        HUD.getInstance(parent).setScore(score);

        captureSound.play();

		/*
         * canCapture = false;
		 * 
		 * if ((captureTask != null) && (captureTask.isScheduled())) {
		 * captureTask.cancel(); } captureTask =
		 * Timer.instance().scheduleTask(new Task() {
		 * 
		 * @Override public void run() { canCapture = true;
		 * captureTask.cancel(); } }, 1);
		 */
    }

    public boolean canBeHurt() {
        return canBeHurt;
    }

    public void hurt() {
        if (!canBeHurt()) {
            return;
        }
        score -= 2;
        canBeHurt = false;

        if ((hurtTask != null) && (hurtTask.isScheduled())) {
            hurtTask.cancel();
        }
        hurtTask = Timer.instance().scheduleTask(new Task() {

            @Override
            public void run() {
                canBeHurt = true;
                hurtTask.cancel();
            }
        }, 2);

        HUD.getInstance(parent).setScore(score);

        hurtSound.play();
    }

    @Override
    public void chooseNextSpot() {
        return;
    }

    @Override
    public void collideWith(MovingCharacter collider) {
        if (collider instanceof Orb) {
            captured(collider);
        } else if (collider instanceof Enemy) {
            hurt();
        }
    }

    @Override
    public void collideWith(Item item) {
        if (item.isInventory()) {
            addToInventory(item);
        }
    }


    @Override
    public void setX(int x) {
        walkSound.play(0.5f);
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        walkSound.play(0.5f);
        super.setY(y);
    }

    public void disable() {
        canCapture = false;
        canBeHurt = false;
        stop();
    }

    public int getScore() {
        return score;
    }

    public void addToInventory(Item item) {
        if (!item.isInventory()) {
            return;
        }

        if (!inventory.contains(item)) {
            System.out.println("Adding " + item + " to inventory");
            pickupSound.play();
            inventory.add(item);
        }
        parent.removeItem(item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void moveNorth() {
        setDirection(3);
        if (isMovementBlocked(new Point(getX(), getY()+1))) {
            return;
        }
        MovingCollision.getInstance().handleCollision();
        setY(getY()+1);
    }

    public void moveSouth() {
        setDirection(0);
        if (isMovementBlocked(new Point(getX(), getY()-1))) {
            return;
        }
        MovingCollision.getInstance().handleCollision();
        setY(getY()-1);
    }

    public void moveEast() {
        setDirection(1);
        if (isMovementBlocked(new Point(getX()+1, getY()))) {
            return;
        }
        MovingCollision.getInstance().handleCollision();
        setX(getX()+1);
    }

    public void moveWest() {
        setDirection(2);
        if (isMovementBlocked(new Point(getX()-1, getY()))) {
            return;
        }
        MovingCollision.getInstance().handleCollision();
        setX(getX()-1);
    }


}
