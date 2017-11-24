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
        this.parent = parent;

        Texture avatarAnimationSheet = new Texture(spriteFilename);
        TextureRegion[][] regions = TextureRegion.split(avatarAnimationSheet, (int) parent.getAvatarLayer().getTileWidth(), (int) parent.getAvatarLayer().getTileWidth());

        if (regions.length <= 0) {
            System.out.println("Error: item pixmap must be at least 1 tile large");
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

        /*eggTimer = Timer.instance().scheduleTask(new Timer.Task() {

            @Override
            public void run() {
                nextState();
                if (state == HATCHED) {
                    eggTimer.cancel();
                    parent.getAvatar().setScore(parent.getAvatar().getScore()+5);
                }
            }
        }, 3, 5);*/

        point = new Point(0,0);
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
        System.out.println("Touch down on egg at "+point);
        //parent.getAvatar().addToInventory(this);
        return true;
    }

}
