package org.noses.game.item;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;
import org.noses.game.path.Point;

import java.util.List;

public abstract class Item {

    Tile[][] tiles;

    // This is the item's origin on the map, in tiles
    Point point;

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

    public boolean overlaps(Item item) {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                if (item.occupies(new Point(x,y))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean occupies(Point point) {
        if (getX() > point.getX()) {
            return false;
        }

        if (getY() > point.getY()) {
            return false;
        }

        if (point.getX() > (getX()+tiles.length)) {
            return false;
        }

        if (point.getY() > (getY()+tiles[0].length)) {
            return false;
        }

        return true;
    }

    public Point findGoodPlace(List<TiledMapTileLayer> obstructionLayers, List<Item> items) {
        boolean found = false;
        Point point = new Point();

        do {
            found = true;
            int randomX = (int)(Math.random()*obstructionLayers.get(0).getWidth());
            int randomY = (int)(Math.random()*obstructionLayers.get(0).getHeight());

            for (TiledMapTileLayer layer : obstructionLayers) {
                if (!canRender(layer)) {
                    found = false;
                    continue;
                }
            }

            for (Item item: items) {
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


    public void startAnimation() {
        animationTask = Timer.schedule(new Timer.Task(){
                           @Override
                           public void run() {
                               for (int x = 0; x < tiles.length; x++) {
                                   for (int y = 0; y < tiles[x].length; y++) {
                                       if (tiles[x][y] != null) {
                                           tiles[x][y].nextFrame();
                                       }
                                   }
                               }
                           }
                       }
                ,0f,1/10.0f);
    }

    public void render(TiledMapTileLayer layer) {
        /*if (!canRender(layer)) {
            System.out.println("Can't render "+this);
            return;
        }*/
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                Tile tile  = tiles[x][y];
                if (tile != null) {
                    tile.render(point, layer);
                }
            }
        }
    }

    public boolean canRender(TiledMapTileLayer layer) {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                Tile tile  = tiles[x][y];
                if (tile != null) {
                    if (!tile.canRender(layer)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public String toString() {
        return tiles.length+"x"+tiles[0].length+" "+getItemName();
    }

    public boolean touchDown(int button) {
        System.out.println (this+" touch down");
        return true;
    }

    public boolean touchUp(int button) {
        System.out.println (this+" touch up");
        return true;
    }

}
