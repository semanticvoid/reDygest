package com.redygest.redundancy.similarity;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.redundancy.similarity.score.ISimilarityScore;

public class BaseLineDataSimilarity implements IDataSimilarity {
	List<ISimilarityScore> scoringFunctions = null;

	public BaseLineDataSimilarity(List<ISimilarityScore> scoringFunctions) {
		this.scoringFunctions = scoringFunctions;
	}

	public double similarity(Data d1, Data d2) {
		double highestScore = 0.0;

		for (ISimilarityScore iss : scoringFunctions) {
			try {
				double curScore = iss.score(d1, d2);
				if (curScore >= 0.5) {
					return 1.0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

}
