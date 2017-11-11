package org.noses.game.ui.highscore;

import java.util.Date;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class HighScoreRepository {
	
	private String password;
	public HighScoreRepository(String mongoPassword) {
		this.password = mongoPassword;
	}

	public void saveScore(String name, int score) {
		Document highScores = new Document();
		highScores.put("name", name);
		highScores.put("score", score);
		highScores.put("datetime", new Date());
		
		password="6vo5iQUiREd7Nao7";
		
		MongoClient mongoClient = new MongoClient(
				new MongoClientURI("mongodb://geldar:"+
						password+
						"@geldar-high-scores-shard-00-00-d506s.mongodb.net:27017,geldar-high-scores-shard-00-01-d506s.mongodb.net:27017,geldar-high-scores-shard-00-02-d506s.mongodb.net:27017/test?ssl=true&replicaSet=Geldar-High-Scores-shard-0&authSource=admin"));
		mongoClient.getDatabase("high_scores").getCollection("scores").insertOne(highScores);
	}
}
