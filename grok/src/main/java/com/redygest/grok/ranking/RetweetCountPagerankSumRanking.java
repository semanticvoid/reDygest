/**
 * 
 */
package com.redygest.grok.ranking;

import java.util.List;
import java.util.Map;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Query;
import com.redygest.commons.data.Tweet;
import com.redygest.commons.util.Math;

/**
 * @author semanticvoid
 * 
 */
public class RetweetCountPagerankSumRanking extends BaseRanking {

	// default for now
	private static final double RTWEIGHT = 0.5;

	protected Map<String, Double> entityPageranks;

	public RetweetCountPagerankSumRanking(List<Data> dataset,
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
			int retweetCount = ((Tweet) d).getRetweetCount();

			List<String> terms = q.getValues(DataType.BODY_TOKENIZED);
			if (terms != null) {
				for (String t : terms) {
					if (this.entityPageranks.containsKey(t)
							&& d.getValue(DataType.ORIGINAL_TEXT).contains(t)) {
						score += this.entityPageranks.get(t);
					}
				}
			}

			// linear comb
			// TODO should normalize pg score?
			score = RTWEIGHT * Math.sigmoid(retweetCount, 0.001, 30)
					+ (1 - RTWEIGHT) * score;
		}

		return score;
	}
}
