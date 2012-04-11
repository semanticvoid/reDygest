package com.redygest.grok.selection.mmr;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
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
	 * @return
	 */
	public List<Data> select(int size) {
		List<Integer> selectedIndices = new ArrayList<Integer>();
		List<Data> selectedData = new ArrayList<Data>();

		// extract only n=size number of items
		for (int i = 0; i < size; i++) {
			// always add index '0' as the first element
			if (i == 0) {
				selectedIndices.add(i);
			} else {
				// bookkeeping for index with max mmr score
				double maxMMRScore = Double.MIN_VALUE;
				int maxMMRScoreIndex = -1;

				// iterate through the entire ranked list
				for (int j = 0; j < rankedData.size(); j++) {
					// ignore rankedData if its already been selected
					if (selectedIndices.contains(j)) {
						continue;
					} else {
						// TODO increment lambda
						double mmrScore = computeMMR(rankedData.get(j),
								selectedIndices, initialLambda);
						// save score and index if max (yet)
						if (mmrScore > maxMMRScore) {
							maxMMRScore = mmrScore;
							maxMMRScoreIndex = j;
						}
					}
				}

				// add max score index as selected
				if (maxMMRScoreIndex != -1) {
					selectedIndices.add(maxMMRScoreIndex);
				}
			}
		}

		// form selected data list
		for (Integer i : selectedIndices) {
			selectedData.add(rankedData.get(i));
		}

		return selectedData;
	}

	/**
	 * Function to compute the MMR score
	 * 
	 * @param d
	 * @param selectedIndices
	 * @param lambda
	 * @return the mmr score
	 */
	protected double computeMMR(Data d, List<Integer> selectedIndices,
			double lambda) {
		double relevanceScore = 0;

		// get relevance score
		if (d != null && d.getValue(DataType.SCORE) != null) {
			relevanceScore = Double.valueOf(d.getValue(DataType.SCORE));
		}

		// get max redundancy score by comparing
		// against already selected data
		double maxRedundancyScore = 0;
		for (Integer i : selectedIndices) {
			Data selectedData = rankedData.get(i);
			double score = this.redundancyScore.score(selectedData, d);
			if (score > maxRedundancyScore) {
				maxRedundancyScore = score;
			}
		}

		// compute mmr score
		double mmrScore = (lambda * relevanceScore)
				- ((1 - lambda) * maxRedundancyScore);
		return mmrScore;
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
