package com.redygest.redundancy.similarity.score;

import com.redygest.commons.data.Data;

public interface ISimilarityScore {
	public double score(Data d1, Data d2);
}
