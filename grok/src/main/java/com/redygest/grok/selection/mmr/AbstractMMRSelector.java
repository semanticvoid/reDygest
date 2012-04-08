package com.redygest.grok.selection.mmr;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Query;
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

	// ranked data
	protected List<Data> rankedData;
	// scoring functions
	protected IScore redundancyScore;

	/**
	 * Constructor
	 */
	public AbstractMMRSelector(List<Data> rankedData, IScore sim2) {
		setRedundancyScore(sim2);
		setRankedData(rankedData);
	}

	/**
	 * Constructor
	 * 
	 * @param rankedData
	 *            the ranked Data list
	 * @param sim2
	 *            the redundancy scoring function
	 * @param initialLambda
	 *            the initial lambda value
	 * @param maxLambda
	 *            the max lambda score
	 */
	public AbstractMMRSelector(List<Data> rankedData, IScore sim2,
			double initialLambda, double maxLambda) {
		if (initialLambda < maxLambda) {
			setInitialLambda(initialLambda);
			setMaxLambda(maxLambda);
		}
		setRedundancyScore(sim2);
		setRankedData(rankedData);
	}

	/**
	 * Select logic
	 * 
	 * @param size
	 * @param data
	 * @param query
	 * @return
	 */
	public List<Data> select(int size, Query query) {
		// TODO basic MMR logic
		return null;
	}

	/**
	 * Select logic
	 * 
	 * @param size
	 * @param data
	 * @return
	 */
	public List<Data> select(int size) {
		// not supported
		return null;
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
	 * @return the rankedData
	 */
	public List<Data> getRankedData() {
		return rankedData;
	}

	/**
	 * @param rankedData
	 *            the rankedData to set
	 */
	public void setRankedData(List<Data> rankedData) {
		this.rankedData = rankedData;
	}

	/**
	 * @return the redundancyScore
	 */
	public IScore getRedundancyScore() {
		return redundancyScore;
	}

	/**
	 * @param redundancyScore
	 *            the redundancyScore to set
	 */
	public void setRedundancyScore(IScore redundancyScore) {
		this.redundancyScore = redundancyScore;
	}

}
