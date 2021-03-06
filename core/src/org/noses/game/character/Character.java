package org.noses.game.character;


import org.noses.game.GeldarGame;
import org.noses.game.item.Item;
import org.noses.game.path.MovingCollision;
import org.noses.game.path.Point;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;

public abstract class Character {
    int x;
    int y;

    int direction;

    int frame;

    TextureRegion[][] animation;

    GeldarGame parent;

    protected Character(String spriteFilename, GeldarGame parent) {

    	this.parent = parent;

        x = 0;
        y = 0;
        direction = 0;
        frame = 0;

        Texture avatarAnimationSheet = new Texture(spriteFilename);
        animation = TextureRegion.split(avatarAnimationSheet, (int)parent.getAvatarLayer().getTileWidth(), (int)parent.getAvatarLayer().getTileHeight());

        Timer.schedule(new Timer.Task(){
                           @Override
                           public void run() {
                               frame++;
                               if(frame >= animation[0].length)
                                   frame = 0;
                           }
                       }
                ,0f,1/10.0f);

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public TextureRegion[][] getAnimation() {
        return animation;
    }

    public void setAnimation(TextureRegion[][] animation) {
        this.animation = animation;
    }

	public boolean isMovementBlocked(Point point) {
		//System.out.println(point+"  isOffscreen="+isOffScreen(point)+" isCollision="+isCollision(point));
		if (isOffScreen(point)) return true;
        if (isCollision(point)) return true;
        if (isOccupiedByItem(point)) return true;
		return false;
	}

	private boolean isOccupiedByItem(Point point) {
        for (Item item: parent.getItems()) {
            if (item.isObstruction()) {
                if (item.occupies(point)) {
                    return true;
                }
            }
        }
        return false;
    }

	private boolean isCollision(Point point) {
		for (TiledMapTileLayer ml: parent.getObstructionLayers()) {
			if (ml.getCell(point.getX(), point.getY()) != null) {
				return true;
			}
		}
		return false;
	}

	protected boolean isOffScreen(Point point) {
		//System.out.println("isOffScreen "+point+".  width="+avatarLayer.getWidth()+" height="+avatarLayer.getHeight());
		if (point.getX() < 0) return true;
		if (point.getX() >= parent.getAvatarLayer().getWidth()) return true;
		if (point.getY() < 0) return true;
		if (point.getY() >= parent.getAvatarLayer().getHeight()) return true;

		return false;
	}
}
