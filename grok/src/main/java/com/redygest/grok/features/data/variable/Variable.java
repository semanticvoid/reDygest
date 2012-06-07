package com.redygest.grok.features.data.variable;

import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;


public interface Variable extends Comparable<Variable>  {
	String getVariableName();
	Long getRecordIdentifier();
	Attributes getVariableAttributes();
	void addAttribute(String attributeName, AttributeId attributeType);
	void addAttributes(Attributes attributes);
	void setAttributes(Attributes attributes);
}
