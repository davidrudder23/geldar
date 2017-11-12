package org.noses.game.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import org.noses.game.path.Point;

public class Tile {

    TextureRegion[] animation;

    // This is the point _relative to the item's point_
    Point point;

    int frame = 0;

    public Tile(Point point, TextureRegion[] animation) {
        this.point = point;
        this.animation = animation;

    }

    public TextureRegion[] getAnimation() {
        return animation;
    }

    public void setAnimation(TextureRegion[] animation) {
        this.animation = animation;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void nextFrame() {
        frame = (frame + 1) % animation.length;
    }

    public void render(Point origin, TiledMapTileLayer layer) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

        cell.setTile(new StaticTiledMapTile(animation[frame]));
        layer.setCell(origin.getX() + point.getX(), origin.getY() + point.getY(), cell);
    }

    public boolean canRender(TiledMapTileLayer layer) {
        return (layer.getCell(point.getX(), point.getY()) == null);
    }
}
