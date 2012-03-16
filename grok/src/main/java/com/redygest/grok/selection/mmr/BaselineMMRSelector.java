package com.redygest.grok.selection.mmr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.selection.mmr.similarity.BaselineSimilarity;

public class BaselineMMRSelector extends AbstractMMRSelector {

	public List<Data> select(List<Data> data, List<String> members) {
		BaselineSimilarity bs = new BaselineSimilarity();

		// init
		double[] scores = new double[data.size()];
		Data d1 = data.get(0);
		List<Integer> selectedIndices = new ArrayList<Integer>();
		selectedIndices.add(0);
		setLambdaValues(0.3, 0.7);
		double LAMBDA = getInitialLambda();
		HashMap<String, Double> cache = new HashMap<String, Double>();
		// iter
		int max_iter = 10;
		int cur_iter = 0;
		while (cur_iter < max_iter) {
			for (int i = 0; i < data.size(); i++) {
				if (selectedIndices.contains(i))
					continue;
				double maxScore = 0;
				for (int j = 0; j < selectedIndices.size(); j++) {
					double curScore;
					if (cache.containsKey(i + ":" + j)) {
						curScore = cache.get(i + ":" + j);
					} else if (cache.containsKey(j + ":" + i)) {
						curScore = cache.get(j + ":" + i);
					} else {
						curScore = bs.sim2(data.get(i).getValues(
								DataType.BODY_TOKENIZED), data.get(
								selectedIndices.get(j)).getValues(
								DataType.BODY_TOKENIZED));
						cache.put(i + ":" + j, curScore);
						cache.put(j + ":" + i, curScore);
					}

					if (maxScore < curScore) {
						maxScore = curScore;
					}
				}
				double queryScore = bs.sim1(data.get(i).getValues(
						DataType.BODY_TOKENIZED), members);
				double mmr_i = ((LAMBDA * queryScore) - ((1 - LAMBDA) * maxScore));
				scores[i] = mmr_i;
			}
			int maxDoc = -1;
			double maxScore = -1.0 * Double.MAX_VALUE;
			for (int i = 0; i < scores.length; i++) {
				if (selectedIndices.contains(i))
					continue;
				if (scores[i] > maxScore) {
					maxDoc = i;
					maxScore = scores[i];
				}
			}
			selectedIndices.add(maxDoc);
			cur_iter++;
			scores = new double[data.size()];
		}

		List<Data> selectedData = new ArrayList<Data>();
		for (int index : selectedIndices) {
			selectedData.add(data.get(index));
		}
		return selectedData;
	}

	public static void main(String[] args) {
		BaselineMMRSelector mmr = new BaselineMMRSelector();

		Date start = new Date();
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(
							"/Users/tejaswi/Documents/workspace/reDygest/sandbox/experiments/1.clinton"));
			String line = null;
			List<Data> data = new ArrayList<Data>();
			int id = 0;
			while ((line = br.readLine()) != null) {
				double pagerank = Double.parseDouble(line.split("\\s+", 3)[1]);
				if (pagerank < 0.015) {
					continue;
				}
				String json = "{\"text\":\"" + line.split("\\s+", 3)[2] + "\"}";
				try {
					id++;
					Data d = new Tweet(json, String.valueOf(id));
					data.add(d);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("Num tweets considered: " + data.size());
			List<String> membership = new ArrayList<String>();
			membership.add("Chelsea");
			membership.add("Chelsea Clinton");
			membership.add("Hillary");
			membership.add("Hillary Clinton");
			membership.add("New York");

			List<Data> selectedData = mmr.select(data, membership);

			System.out
					.println("================Selected Tweet================");
			for (Data tweet : selectedData) {
				System.out.println(tweet.getValue(DataType.BODY));
			}

			Date end = new Date();
			System.out.println("Total time: "
					+ (end.getTime() - start.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
