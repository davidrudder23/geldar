package org.noses.game.ui.highscore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class HighScoreRepository {

	private static HighScoreRepository instance;

	private String password;
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	private HighScoreRepository(String mongoPassword) {
		this.password = mongoPassword;

		mongoClient = new MongoClient(new MongoClientURI("mongodb://geldar:" + password
				+ "@geldar-high-scores-shard-00-00-d506s.mongodb.net:27017,geldar-high-scores-shard-00-01-d506s.mongodb.net:27017,geldar-high-scores-shard-00-02-d506s.mongodb.net:27017/test?ssl=true&replicaSet=Geldar-High-Scores-shard-0&authSource=admin"));

		mongoDatabase = mongoClient.getDatabase("high_scores");
	}

	public static HighScoreRepository getInstance(String mongoPassword) {
		if (instance == null) {
			instance = new HighScoreRepository(mongoPassword);
		}

		return instance;
	}

	public void saveScore(String name, int score) {
		Document highScores = new Document();
		highScores.put("name", name);
		highScores.put("score", score);
		highScores.put("datetime", new Date());

		password = "6vo5iQUiREd7Nao7";

		mongoDatabase.getCollection("scores").insertOne(highScores);
	}

	public List<HighScore> getHighScores() {
		MongoCursor<Document> documents = mongoDatabase.getCollection("scores").find()
				.sort(new BasicDBObject("datetime", -1)).sort(new BasicDBObject("score", -1)).limit(10).iterator();

		List<HighScore> scores = new ArrayList<>();

		while (documents.hasNext()) {
			Document d = documents.next();
			String name = d.getString("name");
			Number score = (Number) d.get("score");
			Date datetime = d.getDate("datetime");
			HighScore highScore = new HighScore(name, score.intValue(), datetime);
			scores.add(highScore);
		}

		return scores;
	}

}
