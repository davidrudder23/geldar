package org.noses.game.character;


import java.util.List;

import org.noses.game.path.Point;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;

public class Character {
    int x;
    int y;

    int direction;

    int frame;

    TextureRegion[][] animation;
	List<TiledMapTileLayer> obstructionLayers;
	TiledMapTileLayer avatarLayer;
    public Character(String spriteFilename, List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
    	this.obstructionLayers = obstructionLayers;
    	this.avatarLayer = avatarLayer;
    	
        x = 0;
        y = 0;
        direction = 0;
        frame = 0;

        Texture avatarAnimationSheet = new Texture("avatar.png");
        animation = TextureRegion.split(avatarAnimationSheet, avatarAnimationSheet.getWidth() / 4, avatarAnimationSheet.getHeight() / 4);

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
    
    public void moveNorth() {
    	setY(getY()+1);
    }

    public void moveSouth() {
    	setY(getY()-1);
    }

    public void moveEast() {
    	setX(getX()+1);
    }

    public void moveWest() {
    	setX(getX()-1);
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

	protected boolean isMovementBlocked(Point point) {
		//System.out.println(point+"  isOffscreen="+isOffScreen(point)+" isCollision="+isCollision(point));
		if (isOffScreen(point)) return true;
		if (isCollision(point)) return true;
		return false;
	}
	
	protected boolean isMovementBlocked(int moveHoriz, int moveVert) {
		if (isOffScreen(moveHoriz, moveVert)) return true;
		if (isCollision(moveHoriz, moveVert)) return true;
		return false;
	}

	private boolean isCollision(int moveHoriz, int moveVert) {
		return isCollision(new Point(getX()+moveHoriz, getY()+moveVert));
	}

	private boolean isCollision(Point point) {
		for (TiledMapTileLayer ml: obstructionLayers) {
			if (ml.getCell(point.getX(), point.getY()) != null) {
				return true;
			}
		}
		return false;
	}

	protected boolean isOffScreen(int moveHoriz, int moveVert) {
		return isOffScreen(new Point(getX()+moveHoriz, getY()+moveVert));
	}
		
	protected boolean isOffScreen(Point point) {
		//System.out.println("isOffScreen "+point+".  width="+avatarLayer.getWidth()+" height="+avatarLayer.getHeight());
		if (point.getX() < 0) return true;
		if (point.getX() >= avatarLayer.getWidth()) return true;
		if (point.getY() < 0) return true;
		if (point.getY() >= avatarLayer.getHeight()) return true;

		return false;
	}
}
