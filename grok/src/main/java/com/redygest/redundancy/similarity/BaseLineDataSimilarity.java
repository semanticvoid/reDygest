package com.redygest.redundancy.similarity;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.redundancy.similarity.score.ISimilarityScore;
import com.redygest.redundancy.similarity.score.SimScoreFactory;
import com.redygest.redundancy.similarity.score.SimScoreFactory.Score;

public class BaseLineDataSimilarity implements IDataSimilarity {
	List<ISimilarityScore> scoringFunctions = null;

	public BaseLineDataSimilarity(List<ISimilarityScore> scoringFunctions) {
		this.scoringFunctions = scoringFunctions;
	}

	public double similarity(Data d1, Data d2) {
		double highestScore = 0.0;
		for (ISimilarityScore iss : scoringFunctions) {
			try {
				double cur_score = iss.score(d1, d2);
				if (cur_score > highestScore) {
					highestScore = cur_score;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (highestScore >= 0.5) {
			return 1.0;
		}
		return 0;
	}

}
