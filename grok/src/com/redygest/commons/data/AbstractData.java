package com.redygest.commons.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractData implements Data {
	private Map<DataType, List<String> > data = new HashMap<DataType, List<String> >();
	
	@Override
	public String getValue(DataType type) {
		assertDataPopulation();
		if(data.get(type) == null || data.get(type).isEmpty()) {
			return null;
		}
		if(data.get(type).size() > 1) {
			throw new RuntimeException(type + " is a multi valued dataType");
		}
		return data.get(type).get(0);
	}
	
	@Override
	public List<String> getValues(DataType type) {
		if(data.get(type) == null || data.get(type).isEmpty()) {
			return null;
		}
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
		data.put(type, Collections.singletonList(value));
	}
	
	@Override
	public void setValues(DataType type, List<String> values) {
		data.put(type, values);
	}
}
