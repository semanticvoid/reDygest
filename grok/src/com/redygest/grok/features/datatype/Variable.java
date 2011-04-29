package com.redygest.grok.features.datatype;


public interface Variable extends Comparable<Variable>  {
	String getVariableName();
	Long getRecordIdentifier();
	Attributes getVariableAttributes();
	void addAttribute(String attributeName, AttributeType attributeType);
	void addAttributes(Attributes attributes);
	void setAttributes(Attributes attributes);
}
