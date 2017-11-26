package org.noses.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import org.noses.game.GeldarGame;
import org.noses.game.character.Avatar;
import org.noses.game.path.Point;

import java.util.List;

public abstract class Item {

    TextureRegion[] animation;

    int frame;

    // This is the item's origin on the map, in tiles
    Point point;

    GeldarGame parent;

    public abstract String getItemName();

    public abstract int getNumPerSecond();

    public abstract boolean isObstruction();

    public abstract boolean isInventory();

    private Timer.Task animationTask;

    public int getX() {
        return point.getX();
    }

    public void setX(int x) {
        point.setX(x);
    }

    public int getY() {
        return point.getY();
    }

    public void setY(int y) {
        point.setY(y);
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public abstract float getNumFramesPerSecond();

    /**
     * Returns 0 for no chance, 1 for 100% chance, 0.5 for 50%
     *
     * @return
     */
    public float getRandomizationChange() {
        return 0;
    }

    public Item(GeldarGame parent, String spriteFilename, Point point) {
        this.parent = parent;
        this.point = point;

        Texture avatarAnimationSheet = new Texture(spriteFilename);
        TextureRegion[][] regions = TextureRegion.split(avatarAnimationSheet, (int) parent.getAvatarLayer().getTileWidth(), (int) parent.getAvatarLayer().getTileWidth());
        animation = regions[0];

        frame = 0;

        animationTask = Timer.schedule(new Timer.Task() {
                                           @Override
                                           public void run() {
                                               if (Math.random() > getRandomizationChange()) {
                                                   frame++;
                                               }
                                               frame %= animation.length;
                                           }
                                       }
                , 0f, 1 / getNumFramesPerSecond());

    }

    public TextureRegion getTextureRegion() {
        return animation[frame];
    }

    public boolean overlaps(Item item) {
        return item.point.equals(point);
    }

    public boolean occupies(Point point) {
        return point.equals(this.point);
    }

    public Point findGoodPlace(List<TiledMapTileLayer> obstructionLayers, List<Item> items) {
        boolean found = false;
        Point point = new Point();


        do {
            found = true;
            int randomX = (int) (Math.random() * obstructionLayers.get(0).getWidth());
            int randomY = (int) (Math.random() * obstructionLayers.get(0).getHeight());

            for (TiledMapTileLayer layer : obstructionLayers) {
                if (layer.getCell(randomX, randomY) != null) {
                    found = false;
                    continue;
                }
            }

            for (Item item : items) {
                if (overlaps(item)) {
                    found = false;
                    continue;
                }
            }

            point.setX(randomX);
            point.setY(randomY);
        } while (!found);

        return point;
    }

    public void collideWith(Avatar avatar) {
        if (isInventory()) {
            avatar.addToInventory(this);
        }
    }

    public abstract void avatarIsNear();

    public void render(TiledMapTileLayer layer) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

        cell.setTile(new StaticTiledMapTile(animation[frame]));
        layer.setCell(point.getX(), point.getY(), cell);
    }

    public String toString() {
        return "Item(" + getItemName() + ")";
    }

    public boolean touchDown(int button) {
        System.out.println(this + " touch down");
        return true;
    }

    public boolean touchUp(int button) {
        System.out.println(this + " touch up");
        return true;
    }

}
