package com.redygest.grok.features.datatype;

import java.io.Serializable;


public interface Variable extends Serializable {
	String getVariableName();
	Long getRecordIdentifier();
	Attributes getVariableAttributes();
	void addAttribute(String attributeName, AttributeType attributeType);
	void addAttributes(Attributes attributes);
	void setAttributes(Attributes attributes);
}
