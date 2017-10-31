package org.noses.game.character;

public class MovingCharacter extends Character {
	
	private int moveToX;
	private int moveToY;
	
    public MovingCharacter(String spriteFilename) {
    	super(spriteFilename);
    }
    
    public void moveTo(int x, int y) {
    	this.moveToX = x;
    	this.moveToY = y;
    }

}
