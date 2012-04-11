/**
 * 
 */
package com.redygest.grok.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Query;

/**
 * Base Ranking Mechanism Class
 * 
 */
public abstract class BaseRanking implements IRanking {

	// dataset to rank
	protected List<Data> dataset;

	/**
	 * Constructor
	 * 
	 * @param dataset
	 */
	public BaseRanking(List<Data> dataset) {
		this.dataset = dataset;
	}

	/**
	 * 
	 */
	public List<Data> rank(Query query) {
		List<Data> rankedList = new ArrayList<Data>();
		if (this.dataset != null && query != null) {
			for (Data d : this.dataset) {
				// score the data
				double score = score(d, query);

				// only if score is > 0
				// i.e. Data is relevant to query
				if (score > 0) {
					// add score to data
					d.setValue(DataType.SCORE, String.valueOf(score));
					rankedList.add(d);
				}
			}

			// rank the list
			Collections.sort(rankedList, new Comparator<Data>() {
				public int compare(Data d1, Data d2) {
					double score1 = 0;
					double score2 = 0;

					if (d1.getValue(DataType.SCORE) != null) {
						score1 = Double.valueOf(d1.getValue(DataType.SCORE));
					}

					if (d2.getValue(DataType.SCORE) != null) {
						score2 = Double.valueOf(d2.getValue(DataType.SCORE));
					}

					if (score1 > score2) {
						return -1;
					} else {
						return +1;
					}
				}
			});
		}

		return rankedList;
	}

	/**
	 * Abstract scoring function to be implmented by subclasses
	 * 
	 * @param d
	 *            the data
	 * @param q
	 *            the query
	 * @return score
	 */
	abstract double score(Data d, Query q);
}
