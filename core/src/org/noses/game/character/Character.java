package org.noses.game.character;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;

public class Character {
    int x;
    int y;

    int direction;

    int frame;

    TextureRegion[][] animation;

    public Character(String spriteFilename) {
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
}
