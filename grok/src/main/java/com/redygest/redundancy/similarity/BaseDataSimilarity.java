package com.redygest.redundancy.similarity;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.redundancy.similarity.score.ISimilarityScore;

public class BaseDataSimilarity implements IDataSimilarity {
	List<ISimilarityScore> scoringFunctions = null;

	public BaseDataSimilarity(List<ISimilarityScore> scoringFunctions) {
		this.scoringFunctions = scoringFunctions;
	}

	public double isSimilar(Data d1, Data d2) {
		return 0.0;
	}

}
