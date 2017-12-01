package org.noses.game.ui.highscore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HighScoreRepository {

	private static HighScoreRepository instance;

	private String password;

	private HighScoreRepository(String mongoPassword) {
		this.password = mongoPassword;
	}

	public static HighScoreRepository getInstance(String mongoPassword) {
		if (instance == null) {
			instance = new HighScoreRepository(mongoPassword);
		}

		return instance;
	}

	public void saveScore(String name, int score) {
	}

	public List<HighScore> getHighScores() {
		List<HighScore> scores = new ArrayList<>();
		return scores;
	}

}
