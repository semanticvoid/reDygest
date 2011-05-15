package com.redygest.commons.data;

public interface Data {
	String getValue(DataType type);
	boolean hasDataType(DataType type);
}
