package com.redygest.score;

import com.redygest.commons.data.Data;

/**
 * Scoring Function Interface
 * 
 */
public interface IScore {

	/**
	 * Scoring function
	 * 
	 * @param d1
	 * @param d2
	 * @return score between d1 & d2 (0-1)
	 */
	public double score(Data d1, Data d2);

}
