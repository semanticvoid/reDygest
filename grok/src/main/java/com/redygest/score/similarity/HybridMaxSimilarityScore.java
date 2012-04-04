package com.redygest.score.similarity;

import com.redygest.commons.data.Data;
import com.redygest.score.IScore;

/**
 * Hybrid Similarity Scoring Function which picks max
 * 
 */
public class HybridMaxSimilarityScore implements IScore {

	// scoring functions
	private IScore[] scoringFunctions = null;

	/**
	 * Constructor
	 * 
	 * @param scoringFunctions
	 */
	public HybridMaxSimilarityScore(IScore... scoringFunctions) {
		this.scoringFunctions = scoringFunctions;
	}

	/**
	 * Scoring function
	 */
	public double score(Data d1, Data d2) {
		double maxScore = 0.0;

		if (scoringFunctions != null) {
			for (IScore iss : scoringFunctions) {
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
