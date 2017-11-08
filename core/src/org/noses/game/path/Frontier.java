package org.noses.game.path;

import java.util.ArrayList;
import java.util.List;

public class Frontier {

	List<PathStep> unSteppedSteps;
	List<PathStep> steppedSteps;

	boolean[][] seen;
	
	Point origin = null;

	int containsCounter = 0;

	public Frontier(Point origin) {
        unSteppedSteps = new ArrayList<>();
        steppedSteps = new ArrayList<>();

        this.origin = origin;

        seen = new boolean[1000][1000];
        for (int x = 0; x < seen.length; x++) {
            for (int y = 0; y < seen[0].length; y++) {
                seen[x][y] = false;
            }
        }
    }

	public void add(PathStep pathStep) {
		if  (origin == null) {
			origin = pathStep.getPoint();
		}
		
		if (contains(pathStep.getPoint())) {
			return;
		}

		seen[pathStep.getPoint().getX()][pathStep.getPoint().getY()] = true;
		
		unSteppedSteps.add(pathStep);

	}

	private void step() {
		//System.out.println("Before step=" + unSteppedSteps);
		steppedSteps.add(unSteppedSteps.get(0));
		unSteppedSteps.remove(0);
		//System.out.println("After step=" + unSteppedSteps);
	}

	public boolean contains(Point point) {
		containsCounter++;
		if (point == null) {
			return false;
		}

		/*for (PathStep step : unSteppedSteps) {
			if (step.getPoint().equals(point)) {
				return true;
			}
		}

		for (PathStep step : steppedSteps) {
			if (step.getPoint().equals(point)) {
				return true;
			}
		}*/

		return seen[point.getX()][point.getY()];
	}

	public int getContainsCounter() {
		return containsCounter;
	}

	public Point nextPoint() {
		if (unSteppedSteps.size() == 0) {
			return null;
		}
		//System.out.println("before step="+unSteppedSteps.get(0));
		step();
		if (unSteppedSteps.size() == 0) {
			return null;
		}
		//System.out.println("after  step="+unSteppedSteps.get(0));
		return unSteppedSteps.get(0).getPoint();
	}

	public PathStep getPathStepOnPoint(Point point) {
		for (PathStep pathStep : unSteppedSteps) {
			if (pathStep.getPoint().equals(point)) {
				return pathStep;
			}
		}

		for (PathStep pathStep : steppedSteps) {
			if (pathStep.getPoint().equals(point)) {
				return pathStep;
			}
		}

		return null;
	}

	public List<Point> getPath(PathStep finalStep) {
		List<Point> path = new ArrayList<>();

		PathStep step = finalStep;
		while (step.getCameFrom() != null) {
			path.add(step.getPoint());
			step = getPathStepOnPoint(step.getCameFrom());
			if (step == null) {
				return path;
			}
		}
		return path;
	}

	public List<Point> getPath(Point finalPoint) {
		return getPath(getPathStepOnPoint(finalPoint));
	}

}
