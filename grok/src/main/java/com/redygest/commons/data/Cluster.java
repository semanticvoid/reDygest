package com.redygest.commons.data;

import java.util.ArrayList;
import java.util.List;
/*
 * List of Data items.
 */
public class Cluster {
	private List<Data> data;

	public Cluster() {
		data = new ArrayList<Data>();
	}

	public void add(Data d) {
		data.add(d);
	}

	public List<Data> getData() {
		return data;
	}
}
