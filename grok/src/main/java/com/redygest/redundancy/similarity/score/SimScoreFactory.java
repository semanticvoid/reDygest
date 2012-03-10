package com.redygest.redundancy.similarity.score;

public class SimScoreFactory {

	public enum Score {
		EXACTDUP, NEARDUP, SEMANTICDUP
	}

	public static ISimilarityScore produceScore(Score e) {
		if (e == Score.EXACTDUP) {
			return new ExactDupSimScore();
		} else if (e == Score.NEARDUP) {
			return new NearDupSimScore(30);
		} else if (e == Score.SEMANTICDUP) {
			return new SemanticSimScore();
		} else {
			// return default
			return new ExactDupSimScore();
		}
	}
}
