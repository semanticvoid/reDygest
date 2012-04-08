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
	 * @param initialLambda
	 * @param maxLambda
	 * @param entityPagerank
	 */
	public BaselineMMRSelector(List<Data> rankedData, double initialLambda,
			double maxLambda, Map<String, Double> entityPagerank) {
		super(rankedData, SimilarityScoreFactory.produceScore(Score.HYBRIDMAX),
				initialLambda, maxLambda);
	}

	// public List<Data> select(List<Data> data, List<String> members) {
	// BaselineSimilarity bs = new BaselineSimilarity();
	//
	// // init
	// double[] scores = new double[data.size()];
	// Data d1 = data.get(0);
	// List<Integer> selectedIndices = new ArrayList<Integer>();
	// selectedIndices.add(0);
	// setLambdaValues(0.3, 0.7);
	// double LAMBDA = getInitialLambda();
	// HashMap<String, Double> cache = new HashMap<String, Double>();
	// // iter
	// int max_iter = 10;
	// int cur_iter = 0;
	// while (cur_iter < max_iter) {
	// for (int i = 0; i < data.size(); i++) {
	// if (selectedIndices.contains(i))
	// continue;
	// double maxScore = 0;
	// for (int j = 0; j < selectedIndices.size(); j++) {
	// double curScore;
	// if (cache.containsKey(i + ":" + j)) {
	// curScore = cache.get(i + ":" + j);
	// } else if (cache.containsKey(j + ":" + i)) {
	// curScore = cache.get(j + ":" + i);
	// } else {
	// curScore = bs.sim2(
	// data.get(i).getValues(DataType.BODY_TOKENIZED),
	// data.get(selectedIndices.get(j)).getValues(
	// DataType.BODY_TOKENIZED));
	// cache.put(i + ":" + j, curScore);
	// cache.put(j + ":" + i, curScore);
	// }
	//
	// if (maxScore < curScore) {
	// maxScore = curScore;
	// }
	// }
	// double queryScore = bs
	// .sim1(data.get(i).getValues(DataType.BODY_TOKENIZED),
	// members);
	// double mmr_i = ((LAMBDA * queryScore) - ((1 - LAMBDA) * maxScore));
	// scores[i] = mmr_i;
	// }
	// int maxDoc = -1;
	// double maxScore = -1.0 * Double.MAX_VALUE;
	// for (int i = 0; i < scores.length; i++) {
	// if (selectedIndices.contains(i))
	// continue;
	// if (scores[i] > maxScore) {
	// maxDoc = i;
	// maxScore = scores[i];
	// }
	// }
	// selectedIndices.add(maxDoc);
	// cur_iter++;
	// scores = new double[data.size()];
	// }
	//
	// List<Data> selectedData = new ArrayList<Data>();
	// for (int index : selectedIndices) {
	// selectedData.add(data.get(index));
	// }
	// return selectedData;
	// }

}
