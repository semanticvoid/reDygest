package com.redygest.grok.selection.mmr;

import java.util.List;
import java.util.Map;

import com.redygest.commons.data.Data;
import com.redygest.score.similarity.SimilarityScoreFactory;
import com.redygest.score.similarity.SimilarityScoreFactory.Score;

/**
 * Baseline MMR Selector
 * 
 * The Baseline MMR (BMMR) uses the entity pageranks for relevance.
 * 
 */
public class BaselineMMRSelector extends AbstractMMRSelector {

	/**
	 * Constructor
	 * 
	 * @param rankedData
	 * @param entityPagerank
	 * @param initialLambda
	 * @param maxLambda
	 */
	public BaselineMMRSelector(List<Data> rankedData,
			Map<String, Double> entityPagerank, double initialLambda,
			double maxLambda) {
		super(rankedData, SimilarityScoreFactory.produceScore(Score.HYBRIDMAX),
				initialLambda, maxLambda);
	}

	/**
	 * Constructor
	 * 
	 * @param rankedData
	 * @param entityPagerank
	 */
	public BaselineMMRSelector(List<Data> rankedData,
			Map<String, Double> entityPagerank) {
		super(rankedData, SimilarityScoreFactory.produceScore(Score.HYBRIDMAX));
	}

}
