package org.noses.game.character;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.noses.game.path.Point;

import java.util.List;

public class Dragon extends MovingCharacter {

    public Dragon(List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
        super("dragon.png", obstructionLayers, avatarLayer);

        x=0;
        y=0;

        chooseNextSpot();
    }

    private void chooseNextSpot() {
        Point point = null;
        do {
            int x = (int)(Math.random()* avatarLayer.getWidth());
            int y = (int)(Math.random()* avatarLayer.getHeight());

            point = new Point(x,y);
        } while (isMovementBlocked(point));

        System.out.println("Moving dragon to "+point);
        moveTo(point);
    }

    @Override
    protected void stopWalking() {
        super.stopWalking();
        chooseNextSpot();
    }
}
