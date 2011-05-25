package com.redygest.commons.data;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractData implements Data {
	protected Map<DataType, String> data = new HashMap<DataType, String>();
	
	@Override
	public String getValue(DataType type) {
		assertDataPopulation();
		return data.get(type);
	}
	
	@Override
	public boolean hasDataType(DataType type) {
		assertDataPopulation();
		return data.containsKey(type);
	}
	
	protected abstract boolean isDataPopulated();
	
	private void assertDataPopulation() {
		if(!isDataPopulated()) {
			throw new RuntimeException("Data is not populated");
		}
	}
	
	@Override
	public void setValue(DataType type, String value) {
		data.put(type, value);
	}
}
