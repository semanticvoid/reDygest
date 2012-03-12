package com.redygest.redundancy.selection;

import java.util.List;

import com.redygest.commons.data.Cluster;
import com.redygest.commons.data.Data;

public interface ISelector {
	List<Data> select(List<Cluster> clusters);

}
