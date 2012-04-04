package com.redygest.score.similarity;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.score.IScore;

public class ExactDupSimilarityScore implements IScore {

	public double score(Data d1, Data d2) {
		List<String> toks1 = d1.getValues(DataType.BODY_TOKENIZED);
		List<String> toks2 = d2.getValues(DataType.BODY_TOKENIZED);
		if (toks1.size() != toks2.size()) {
			return 0;
		}
		for (int i = 0; i < toks1.size(); i++) {
			if (!toks1.get(i).equalsIgnoreCase(toks2.get(i))) {
				return 0;
			}
		}
		return 1;
	}

}
