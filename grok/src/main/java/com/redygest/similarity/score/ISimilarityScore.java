package com.redygest.similarity.score;

import com.redygest.commons.data.Data;

/**
 * Similarity Scoring Function Interface
 * 
 */
public interface ISimilarityScore {

	/**
	 * Scoring function
	 * 
	 * @param d1
	 * @param d2
	 * @return sim score between d1 & d2 (0-1)
	 */
	public double score(Data d1, Data d2);

}
