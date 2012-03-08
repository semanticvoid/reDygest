package com.redygest.redundancy.similarity;

import com.redygest.commons.data.Data;

public interface IDataSimilarity {
	double isSimilar(Data d1, Data d2);
}
