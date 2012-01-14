/**
 * 
 */
package com.redygest.commons.db.similarity;


public interface ISimilarityDb {
	
	/**
	 * Get similarity score
	 * @param w1
	 * @param w2
	 * @return	sim score
	 */
	public double getSimilarity(String w1, String w2);
	
}
