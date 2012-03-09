package com.redygest.redundancy.similarity;

import com.redygest.commons.data.Data;

public interface IDataSimilarity {
	double similarity(Data d1, Data d2);
}
