package com.redygest.redundancy.similarity.score;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.similarity.score.ISimilarityScore;
import com.redygest.similarity.score.SimilarityScoreFactory;
import com.redygest.similarity.score.SimilarityScoreFactory.Score;

public class ExactDupSimScoreTest extends TestCase {

	List<Data> data = new ArrayList<Data>();
	ISimilarityScore scoreFn;

	@Override
	protected void setUp() {
		scoreFn = SimilarityScoreFactory.produceScore(Score.EXACTDUP);
		Data d1 = new Tweet("{\"text\":\"Bill Clinton went to Washington.\"}",
				"1");
		Data d2 = new Tweet("{\"text\":\"Bill Clinton went to Washington.\"}",
				"1");
		data.add(d1);
		data.add(d2);
	}

	public void testExactDup() {
		double score = scoreFn.score(data.get(0), data.get(1));
		assertEquals(1.0, score, 0);
	}
}
