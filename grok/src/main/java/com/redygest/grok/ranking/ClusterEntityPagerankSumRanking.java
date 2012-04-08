/**
 * 
 */
package com.redygest.grok.ranking;

import java.util.List;
import java.util.Map;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Query;

/**
 * @author semanticvoid
 * 
 */
public class ClusterEntityPagerankSumRanking extends BaseRanking {

	protected Map<String, Double> entityPageranks;

	public ClusterEntityPagerankSumRanking(List<Data> dataset,
			Map<String, Double> pageranks) {
		super(dataset);
		this.entityPageranks = pageranks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.grok.ranking.BaseRanking#score(com.redygest.commons.data
	 * .Data, com.redygest.commons.data.Query)
	 */
	@Override
	double score(Data d, Query q) {
		double score = 0;

		if (q != null && d != null) {
			List<String> terms = q.getValues(DataType.BODY_TOKENIZED);
			if (terms != null) {
				for (String t : terms) {
					if (this.entityPageranks.containsKey(t)) {
						score += this.entityPageranks.get(t);
					}
				}
			}
		}

		return score;
	}

}
