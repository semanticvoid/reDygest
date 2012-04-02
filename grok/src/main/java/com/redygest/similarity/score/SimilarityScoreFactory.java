package com.redygest.similarity.score;

/**
 * Similarity Score Factory
 * 
 */
public class SimilarityScoreFactory {

	/**
	 * Score Function Enum
	 */
	public enum Score {
		EXACTDUP, NEARDUP, PHRASENEARDUP, SEMANTICDUP, HYBRIDMAX;
	}

	/**
	 * Produce sim score
	 * 
	 * @param score
	 *            function type
	 * @return {@link ISimilarityScore}
	 */
	public static ISimilarityScore produceScore(Score e) {

		switch (e) {
		case EXACTDUP:
			return new ExactDupSimilarityScore();
		case NEARDUP:
			return new NearDuplicateSimilarityScore(30);
		case PHRASENEARDUP:
			return new PhraseNearDuplicateSimilarityScore(30);
		case SEMANTICDUP:
			return new SemanticSimilarityScore();
		case HYBRIDMAX:
			return new HybridMaxSimilarityScore(
					SimilarityScoreFactory.produceScore(Score.EXACTDUP),
					SimilarityScoreFactory.produceScore(Score.NEARDUP),
					SimilarityScoreFactory.produceScore(Score.PHRASENEARDUP));
		}

		return null;
	}
}
