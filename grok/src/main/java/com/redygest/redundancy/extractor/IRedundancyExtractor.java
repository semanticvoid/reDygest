package com.redygest.redundancy.extractor;

import java.util.List;

import com.redygest.commons.data.Cluster;
import com.redygest.commons.data.Data;

public interface IRedundancyExtractor {
	public List<Cluster> cluster(List<Data> lines);

}
