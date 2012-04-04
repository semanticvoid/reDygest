package com.redygest.redundancy.similarity.score;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.score.IScore;
import com.redygest.score.similarity.SimilarityScoreFactory;
import com.redygest.score.similarity.SimilarityScoreFactory.Score;

public class PhraseNearDupSimScoreTest extends TestCase {

	IScore scoreFn;

	@Override
	protected void setUp() {
		scoreFn = SimilarityScoreFactory.produceScore(Score.PHRASENEARDUP);
	}

	public void testPhraseSim() {
		Data d1 = new Tweet(
				"{\"text\":\"Bill Clinton undergoes heart procedure: Former President Bill Clinton had two stents inserted\u2026 http://goo.gl/fb/wH\"}",
				"1");
		Data d2 = new Tweet(
				"{\"text\":\"Bill Clinton undergoes heart procedure\"}", "2");

		double score = scoreFn.score(d1, d2);
		assertEquals(1.0, score);
	}
}
