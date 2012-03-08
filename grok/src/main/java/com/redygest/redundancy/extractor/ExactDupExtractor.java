package com.redygest.redundancy.extractor;

import java.util.List;

import com.redygest.commons.data.Cluster;
import com.redygest.commons.data.Data;

public class ExactDupExtractor implements IRedundancyExtractor {

	/*
	 * (non-Javadoc)
	 * @see com.redygest.redundancy.extractor.IRedundancyExtractor#remove(java.util.List)
	 * clusters exact duplicates
	 */
	public List<Cluster> cluster(List<Data> lines) {
		return null;
	}

}
