package org.noses.game.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.noses.game.GeldarGame;
import org.noses.game.item.Item;
import org.noses.game.path.Frontier;
import org.noses.game.path.MovingCollision;
import org.noses.game.path.PathStep;
import org.noses.game.path.Point;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public abstract class MovingCharacter extends Character {

	private List<Point> path;
	private int pathStep;
	private Task movingTask;

	protected MovingCharacter(String spriteFilename, GeldarGame parent) {
		super(spriteFilename, parent);

		Point startingPoint = findAGoodSpot();
		x = startingPoint.getX();
		y = startingPoint.getY();
	}

	protected abstract float getNumPerSecond();

	public void moveTo(Point point) {
		stopWalking();

		path = getPath(point);
		pathStep = 0;

		movingTask = Timer.schedule(new Timer.Task() {

			@Override
			public void run() {
				walk();
			}
		}, 0f, 1 / getNumPerSecond());
	}

	public void stop() {
		stopWalking();
	}

	protected Point findAGoodSpot() {
		Point point = null;

		do {
			int x = (int) (Math.random() * parent.getAvatarLayer().getWidth());
			int y = (int) (Math.random() * parent.getAvatarLayer().getHeight());

			point = new Point(x, y);
		} while (isMovementBlocked(point));

		return point;
	}

	public abstract void chooseNextSpot();

	protected void walk() {
		if (path == null) {
			return;
		}
		Point nextPoint = path.get(pathStep);
		// System.out.println("Next point="+nextPoint);
		if (isMovementBlocked(nextPoint)) {
			System.out.println("Can't move because blocked");
			return;
		}

		x = nextPoint.getX();
		y = nextPoint.getY();

		pathStep++;

		if (pathStep >= path.size()) {
			stopWalking();
			chooseNextSpot();
		}

		MovingCollision.getInstance().handleCollision();

	}

	protected void stopWalking() {
		path = null;
		pathStep = 0;
		if ((movingTask != null) && (movingTask.isScheduled())) {
			movingTask.cancel();
		}
	}

	public abstract void collideWith(MovingCharacter collider);

	public abstract void collideWith(Item item);

	/**
	 * Finds the best path from from to to using a simplified djikstra's algo
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Point> getPath(Point from, Point to) {
		// long start = System.currentTimeMillis();
		if (isMovementBlocked(to)) {
			System.out.println("Movement is blocked");
			return null;
		}

		if (from.equals(to)) {
			return Collections.singletonList(from);
		}

		Frontier frontier = new Frontier(from);

		frontier.add(new PathStep(to, null));

		Point current = to;

		while (!from.equals(current)) {

			List<Point> neighbors = new ArrayList<>();
			neighbors.add(new Point(current.getX(), current.getY() - 1));
			neighbors.add(new Point(current.getX(), current.getY() + 1));
			neighbors.add(new Point(current.getX() - 1, current.getY()));
			neighbors.add(new Point(current.getX() + 1, current.getY()));

			/*
			 * neighbors.add(new Point(current.getX() - 1, current.getY() - 1));
			 * neighbors.add(new Point(current.getX() + 1, current.getY() - 1));
			 * neighbors.add(new Point(current.getX() - 1, current.getY() - 1));
			 * neighbors.add(new Point(current.getX() - 1, current.getY() + 1));
			 */

			final Point finalCurrent = current;

			neighbors.stream().filter(point -> !isMovementBlocked(point))
					.map(point -> new PathStep(point, finalCurrent)).forEach(pathStep -> frontier.add(pathStep));

			current = frontier.nextPoint();

			// System.out.println("from ="+from);
			// System.out.println("current="+current);

			if (current == null) {
				// long end = System.currentTimeMillis();
				// System.out.println("Getting null path from " + from + " to "
				// + to + " took " + (end - start) + " millis");
				// System.out.println("Called null contains " +
				// frontier.getContainsCounter() + " times");
				return null;
			}
		}

		// long end = System.currentTimeMillis();
		// System.out.println("Getting path from " + from + " to " + to + " took
		// " + (end - start) + " millis");
		// System.out.println("Called contains " + frontier.getContainsCounter()
		// + " times");

		return frontier.getPath(current);
	}

	public List<Point> getPath(Point to) {
		return getPath(new Point(getX(), getY()), to);
	}
}
