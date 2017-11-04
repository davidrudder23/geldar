package org.noses.game.path;

public class PathStep {

	private Point point;
	private Point cameFrom;
	
	public PathStep(Point point, Point cameFrom) {
		this.point = point;
		this.cameFrom = cameFrom;
	}

	public Point getPoint() {
		return point;
	}

	public Point getCameFrom() {
		return cameFrom;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PathStep)) {
			return false;
		}
		
		PathStep other = (PathStep)obj;
		
		return getPoint().equals(other.getPoint());
	}
	
	
	@Override
	public String toString() {
		return "point="+getPoint()+" cameFrom="+cameFrom;
	}	
}
