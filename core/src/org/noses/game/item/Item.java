package org.noses.game.item;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;
import org.noses.game.path.Point;

public abstract class Item {

    Tile[][] tiles;

    // This is the item's origin on the map, in tiles
    Point point;

    public abstract int getNumPerSecond();

    public abstract boolean isObstruction();

    private Timer.Task animationTask;

    public int getX() {
        return point.getX();
    }

    public int getY() {
        return point.getY();
    }

    public boolean occupies(Point point) {
        if (getX() > point.getX()) {
            return false;
        }

        if (getY() > point.getY()) {
            return false;
        }

        if (point.getX()> (getX()+tiles.length)) {
            return false;
        }

        if (point.getY()> (getY()+tiles[0].length)) {
            return false;
        }

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                Tile tile = tiles[x][y];

                if (tile == null) {
                    continue;
                }
                Point comparison = new Point(getX()+tile.getPoint().getX(), getY()+tile.getPoint().getY());
                if (point.equals(comparison)) {
                    return true;
                }
            }
        }

        return false;
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
        return tiles.length+"x"+tiles[0].length+" tile";
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
