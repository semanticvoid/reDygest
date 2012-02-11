package com.redygest.commons.data;

import java.util.List;

public interface Data {
	String getValue(DataType type);
	List<String> getValues(DataType type);
	boolean hasDataType(DataType type);
	void setValue(DataType type, String value);
	public void setValues(DataType type, List<String> values);
}
