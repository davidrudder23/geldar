package org.noses.game.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.noses.game.path.Frontier;
import org.noses.game.path.PathStep;
import org.noses.game.path.Point;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class MovingCharacter extends Character {

	private int moveToX;
	private int moveToY;

	public MovingCharacter(String spriteFilename, List<TiledMapTileLayer> obstructionLayers, TiledMapTileLayer avatarLayer) {
		super(spriteFilename, obstructionLayers, avatarLayer);

		/*
		 * Timer.schedule(new Timer.Task() {
		 * 
		 * @Override public void run() { walk(); } }, 0f, 1/2.0f);
		 */
	}

	public void moveTo(int x, int y) {
		this.moveToX = x;
		this.moveToY = y;
	}

	public void walk() {
		int randomNum = (int) (Math.random() * 4);

		System.out.println("random=" + randomNum);

		if ((randomNum == 0) && (!isMovementBlocked(getX() - 1, getY()))) {
			x--;
			return;
		}
		if ((randomNum == 1) && (!isMovementBlocked(getX(), getY() + 1))) {
			y++;
			return;
		}

		if ((randomNum == 2) && (!isMovementBlocked(getX() + 1, getY()))) {
			x++;
			return;
		}

		if ((randomNum == 3) && (!isMovementBlocked(getX(), getY() - 1))) {
			y--;
			return;
		}
	}

	/**
	 * Finds the best path from from to to using a simplified djikstra's algo
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Point> getPath(Point from, Point to) {
		long start = System.currentTimeMillis();
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
			neighbors.add(new Point(current.getX() - 1, current.getY() - 1));
			neighbors.add(new Point(current.getX() + 1, current.getY() - 1));
			neighbors.add(new Point(current.getX() - 1, current.getY() - 1));
			neighbors.add(new Point(current.getX() - 1, current.getY() + 1));
			 */
			
			final Point finalCurrent = current;

			neighbors.stream().filter(point -> !isMovementBlocked(point))
			.map(point -> new PathStep(point, finalCurrent))
			.forEach(pathStep -> frontier.add(pathStep));

			current = frontier.nextPoint();

			// System.out.println("from ="+from);
			// System.out.println("current="+current);

			if (current == null) {
				//long end = System.currentTimeMillis();
				//System.out.println("Getting null path from " + from + " to " + to + " took " + (end - start) + " millis");
				//System.out.println("Called null contains " + frontier.getContainsCounter() + " times");
				return null;
			}
		}

		long end = System.currentTimeMillis();
		//System.out.println("Getting path from " + from + " to " + to + " took " + (end - start) + " millis");
		//System.out.println("Called contains " + frontier.getContainsCounter() + " times");

		return frontier.getPath(current);
	}

	public List<Point> getPath(Point to) {
		return getPath(new Point(getX(), getY()), to);
	}
}
