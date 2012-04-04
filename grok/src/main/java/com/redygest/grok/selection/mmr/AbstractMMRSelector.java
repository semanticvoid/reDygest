package com.redygest.grok.selection.mmr;

import com.redygest.grok.selection.ISelector;
import com.redygest.score.IScore;

/**
 * Abstract MMR class
 * 
 * Lamda*sim1 - (1-Lambda)*sim2
 * 
 */
public abstract class AbstractMMRSelector implements ISelector {

	// lambda values
	protected double initialLambda = 0.3; // default
	protected double maxLambda = 0.4; // default

	// scoring functions
	protected IScore sim1;
	protected IScore sim2;

	/**
	 * Constructor
	 */
	public AbstractMMRSelector(IScore sim1, IScore sim2) {
		setSim1(sim1);
		setSim2(sim2);
	}

	/**
	 * Constructor
	 * 
	 * @param sim1
	 *            the relevance scoring function
	 * @param sim2
	 *            the redundancy scoring function
	 * @param initialLambda
	 *            the initial lambda value
	 * @param maxLambda
	 *            the max lambda score
	 */
	public AbstractMMRSelector(IScore sim1, IScore sim2, double initialLambda,
			double maxLambda) {
		if (initialLambda < maxLambda) {
			setInitialLambda(initialLambda);
			setMaxLambda(maxLambda);
		}
		setSim1(sim1);
		setSim2(sim2);
	}

	/**
	 * @return the initialLambda
	 */
	public double getInitialLambda() {
		return initialLambda;
	}

	/**
	 * @param initialLambda
	 *            the initialLambda to set
	 */
	public void setInitialLambda(double initialLambda) {
		if (initialLambda <= 1)
			this.initialLambda = initialLambda;
	}

	/**
	 * @return the maxLambda
	 */
	public double getMaxLambda() {
		return maxLambda;
	}

	/**
	 * @param maxLambda
	 *            the maxLambda to set
	 */
	public void setMaxLambda(double maxLambda) {
		if (maxLambda <= 1)
			this.maxLambda = maxLambda;
	}

	/**
	 * @return the sim1
	 */
	public IScore getSim1() {
		return sim1;
	}

	/**
	 * @param sim1
	 *            the sim1 to set
	 */
	public void setSim1(IScore sim1) {
		this.sim1 = sim1;
	}

	/**
	 * @return the sim2
	 */
	public IScore getSim2() {
		return sim2;
	}

	/**
	 * @param sim2
	 *            the sim2 to set
	 */
	public void setSim2(IScore sim2) {
		this.sim2 = sim2;
	}

}
