/**
 * 
 */
package com.redygest.commons.db.similarity;

import java.util.Map;


public interface ISimilarityDb {
	
	/**
	 * Get similarity score
	 * @param w1
	 * @param w2
	 * @return	sim score 0 to 1, -1 on error
	 */
	public Map<String, Double> getSimilarity(String w1, String w2);
	
}
