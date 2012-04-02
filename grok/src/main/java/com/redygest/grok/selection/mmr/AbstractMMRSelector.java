package com.redygest.grok.selection.mmr;

import com.redygest.grok.selection.ISelector;
import com.redygest.similarity.score.ISimilarityScore;

/**
 * Abstract MMR class
 * 
 * Lamda*sim1 - (1-Lambda)*sim2
 * 
 */
public abstract class AbstractMMRSelector implements ISelector {

	// lambda values
	protected double initialLambda;
	protected double maxLambda;

	// similarity functions

	/**
	 * Constructor
	 */
	public AbstractMMRSelector(ISimilarityScore sim1, ISimilarityScore sim2) {
		this.initialLambda = 0.3; // default
		this.maxLambda = 0.4; // default
	}

	/**
	 * Constructor
	 * 
	 * @param initialLambda
	 * @param maxLambda
	 */
	public AbstractMMRSelector(double initialLambda, double maxLambda) {
		this.initialLambda = initialLambda;
		this.maxLambda = maxLambda;
	}

}
