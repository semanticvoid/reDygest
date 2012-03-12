package com.redygest.redundancy.similarity.score;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.redundancy.similarity.score.SimScoreFactory.Score;

public class NearDupSimScoreTest extends TestCase {

	ISimilarityScore scoreFn;

	@Override
	protected void setUp() {
		scoreFn = SimScoreFactory.produceScore(Score.NEARDUP);
	}

	public void testNearDup() {
		Data d1 = new Tweet("{\"text\":\"1 2 A 4 5 6, 7 8, 9 0\"}", "1");
		Data d2 = new Tweet("{\"text\":\"1 2 a 4, 5 6 7 8 9 0\"}", "2");
		double score = scoreFn.score(d1, d2);
		assertEquals(1.0, score, 0);

		Data d3 = new Tweet("{\"text\":\"1 2 3 4 5 10 11 8 9 0\"}", "2");
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

}
