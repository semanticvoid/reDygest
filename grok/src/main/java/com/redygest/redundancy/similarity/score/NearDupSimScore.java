package com.redygest.redundancy.similarity.score;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;

public class NearDupSimScore implements ISimilarityScore {
	public double score(Data d1, Data d2) {
		String text1 = d1.getValue(DataType.BODY_TOKENIZED);
		String text2 = d2.getValue(DataType.BODY_TOKENIZED);
		return 0;
	}

}
