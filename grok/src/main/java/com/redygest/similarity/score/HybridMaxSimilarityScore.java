package com.redygest.similarity.score;

import com.redygest.commons.data.Data;

/**
 * Hybrid Similarity Scoring Function which picks max
 * 
 */
public class HybridMaxSimilarityScore implements ISimilarityScore {

	// scoring functions
	private ISimilarityScore[] scoringFunctions = null;

	/**
	 * Constructor
	 * 
	 * @param scoringFunctions
	 */
	public HybridMaxSimilarityScore(ISimilarityScore... scoringFunctions) {
		this.scoringFunctions = scoringFunctions;
	}

	/**
	 * Scoring function
	 */
	public double score(Data d1, Data d2) {
		double maxScore = 0.0;

		if (scoringFunctions != null) {
			for (ISimilarityScore iss : scoringFunctions) {
				try {
					double curScore = iss.score(d1, d2);
					if (curScore > maxScore) {
						maxScore = curScore;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return maxScore;
	}

}
