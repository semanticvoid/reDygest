package com.redygest.redundancy.clustering;

import java.util.List;

import com.redygest.commons.data.Cluster;
import com.redygest.commons.data.Data;

public interface IClustering {

	List<Cluster> cluster(List<Data> data);
}
