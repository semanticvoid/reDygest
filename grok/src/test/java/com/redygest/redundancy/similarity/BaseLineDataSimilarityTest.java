package com.redygest.redundancy.similarity;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.redundancy.similarity.score.ISimilarityScore;
import com.redygest.redundancy.similarity.score.SimScoreFactory;
import com.redygest.redundancy.similarity.score.SimScoreFactory.Score;

import junit.framework.TestCase;

public class BaseLineDataSimilarityTest extends TestCase {
	IDataSimilarity sim = null;

	@Override
	protected void setUp() {
		List<ISimilarityScore> scoringFunctions = new ArrayList<ISimilarityScore>();
		scoringFunctions.add(SimScoreFactory.produceScore(Score.EXACTDUP));
		scoringFunctions.add(SimScoreFactory.produceScore(Score.NEARDUP));
		scoringFunctions.add(SimScoreFactory.produceScore(Score.PHRASENEARDUP));
		sim = new BaseLineDataSimilarity(scoringFunctions);
	}

	public void testDataSimilarity() {
		Data d1 = new Tweet("{\"text\":\"Bill Clinton went to Washington.\"}",
				"1");
		Data d2 = new Tweet("{\"text\":\"Bill Clinton went to Washington.\"}",
				"1");
		double score = sim.similarity(d1, d2);
		assertEquals(1.0, score, 0);
	}

}
