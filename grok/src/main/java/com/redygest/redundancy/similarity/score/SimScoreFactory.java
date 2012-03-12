package com.redygest.redundancy.similarity.score;

public class SimScoreFactory {

	public enum Score {
		EXACTDUP, NEARDUP, PHRASENEARDUP, SEMANTICDUP
	}

	public static ISimilarityScore produceScore(Score e) {
		if (e == Score.EXACTDUP) {
			return new ExactDupSimScore();
		} else if (e == Score.NEARDUP) {
			return new NearDupSimScore(100);
		} else if (e == Score.PHRASENEARDUP) {
			return new PhraseNearDupSimScore(100);
		} else if (e == Score.SEMANTICDUP) {
			return new SemanticSimScore();
		} else {
			// return default
			return new ExactDupSimScore();
		}
	}
}
