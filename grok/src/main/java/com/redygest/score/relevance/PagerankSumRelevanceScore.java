/**
 * 
 */
package com.redygest.score.relevance;

import java.util.Map;

import com.redygest.commons.data.Data;
import com.redygest.score.IScore;

/**
 * Pagerank Sum Relevance Score
 * 
 * This scoring function returns the sum of pageranks of Query terms found in
 * the Data.
 * 
 */
public class PagerankSumRelevanceScore implements IScore {

	// term pagerank map
	private Map<String, Double> pageranks;

	/**
	 * Constructor
	 * 
	 * @param pageranks
	 *            pagerank map
	 */
	public PagerankSumRelevanceScore(Map<String, Double> pageranks) {
		setPageranks(pageranks);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.score.IScore#score(com.redygest.commons.data.Data,
	 * com.redygest.commons.data.Data)
	 */
	public double score(Data d1, Data query) {
		// TODO
		return 0;
	}

	/**
	 * @return the pageranks
	 */
	public Map<String, Double> getPageranks() {
		return pageranks;
	}

	/**
	 * @param pageranks
	 *            the pageranks to set
	 */
	private void setPageranks(Map<String, Double> pageranks) {
		this.pageranks = pageranks;
	}

}
