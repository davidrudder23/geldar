package org.noses.game.path;

public class Point {

	private int x;

	private int y;

	public Point() {

	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point)) {
			return false;
		}

		Point other = (Point) obj;
		return ((other.getX() == getX()) && (other.getY() == getY()));
	}

	@Override
	public int hashCode() {
		return x * 100 + y;
	}

	@Override
	public String toString() {
		return "(" + getX() + "," + getY() + ")";
	}
	
	public int manhattenDistance(Point origin) {
		int distanceX = Math.abs(getX() - origin.getX());
		int distanceY = Math.abs(getY() - origin.getY());
		return (distanceX + distanceY);
	}
}
