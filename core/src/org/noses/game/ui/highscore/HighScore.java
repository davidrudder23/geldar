package org.noses.game.ui.highscore;

import java.util.Date;

public class HighScore {

	private String name;
	private int score;
	private Date date;

	public HighScore(String name, int score, Date date) {
		this.name = name;
		this.score = score;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return name + ": " + score + " on " + date;
	}

}
