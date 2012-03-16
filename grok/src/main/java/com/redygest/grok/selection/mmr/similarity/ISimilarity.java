package com.redygest.grok.selection.mmr.similarity;

import java.util.List;

public interface ISimilarity {
	public double sim1(List<String> tokens1, List<String> tokens2);

	public double sim2(List<String> tokens1, List<String> tokens2);
}
