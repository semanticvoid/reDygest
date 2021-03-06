package com.redygest.redundancy.similarity.score;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.score.IScore;
import com.redygest.score.similarity.SimilarityScoreFactory;
import com.redygest.score.similarity.SimilarityScoreFactory.Score;

public class NearDupSimScoreTest extends TestCase {

	IScore scoreFn;

	@Override
	protected void setUp() {
		scoreFn = SimilarityScoreFactory.produceScore(Score.NEARDUP);
	}

	public void testNearDup() {
		Data d1 = new Tweet("{\"text\":\"1 2 A 4 5 6, 7 8, 9 0\"}", "1");
		Data d2 = new Tweet("{\"text\":\"1 2 a 4, 5 6 7 8 9 0\"}", "2");
		double score = scoreFn.score(d1, d2);
		assertEquals(1.0, score, 0);

		Data d3 = new Tweet("{\"text\":\"1 2 B 4 5 6, 7 8, 9 0\"}", "2");
		score = scoreFn.score(d1, d3);
		if (score < 0.5 || score == 1) {
			fail();
		}
	}

	public void testOrderSwap() {
		Data d1 = new Tweet(
				"{\"text\":\"Hilary arrives home after heart surgery http://bit.ly/buIScC #US #politics #news\"}",
				"1");
		Data d2 = new Tweet(
				"{\"text\":\"Clinton arrives home after heart surgery http://bit.ly/buIScC #US #politics #news\"}",
				"2");
		double score1 = scoreFn.score(d1, d2);
		double score2 = scoreFn.score(d2, d1);
		assertEquals(score1, score2);
	}

	public void testNoSimilarity() {
		Data d1 = new Tweet("{\"text\":\"tt is a fool with good thing\"}", "1");
		Data d2 = new Tweet("{\"text\":\"foolish things happen\"}", "2");
		double score = scoreFn.score(d1, d2);
		assertEquals(0.0, score);
	}

	public void testNoSigsForText() {
		Data d1 = new Tweet(
				"{\"text\":\"Jethmalani refers to Rajiv Gandhi as having a Swiss bank account, revealed by Swiss Banks !!!CON\"}",
				"1");
		Data d2 = new Tweet(
				"{\"text\":\"#Jethmalani is on fire at the #RajyaSabha #Lokpal\"}",
				"2");
		double score = scoreFn.score(d1, d2);
		assertEquals(0.0, score);
	}
}
