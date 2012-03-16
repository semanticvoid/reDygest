package com.redygest.grok.selection.mmr.similarity;

import java.util.ArrayList;
import java.util.List;

public class BaselineSimilarity implements ISimilarity {

	private double jaccardSim(List<String> tokens1, List<String> tokens2) {

		if (tokens1 == null || tokens2 == null) {
			return 0;
		}

		List<String> tokens_1 = new ArrayList<String>();
		List<String> tokens_2 = new ArrayList<String>();
		for (String token : tokens1) {
			if (!token.contains("http")) {
				tokens_1.add(token);
			}
		}
		for (String token : tokens2) {
			if (!token.contains("http")) {
				tokens_2.add(token);
			}
		}

		tokens1 = tokens_1;
		tokens2 = tokens_2;

		int aNb = 0;
		for (int i = 0; i < tokens1.size(); i++) {
			for (int j = 0; j < tokens2.size(); j++) {
				if (tokens1.get(i).equalsIgnoreCase(tokens2.get(j))) {
					aNb++;
					break;
				}
			}
		}

		for (int i = 0; i < tokens2.size(); i++) {
			for (int j = 0; j < tokens1.size(); j++) {
				if (tokens2.get(i).equalsIgnoreCase(tokens1.get(j))) {
					aNb++;
					break;
				}
			}
		}

		double sim = ((aNb * 1.0) / (tokens1.size() + tokens2.size()));
		if (sim > 1) {
			sim = 1;
		}
		return sim;
	}

	public double sim1(List<String> tokens1, List<String> tokens2) {
		// TODO Auto-generated method stub
		return jaccardSim(tokens1, tokens2);
	}

	public double sim2(List<String> tokens1, List<String> tokens2) {
		// TODO Auto-generated method stub
		return jaccardSim(tokens1, tokens2);
	}

}
