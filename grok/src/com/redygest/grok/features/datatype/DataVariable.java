package com.redygest.grok.features.datatype;


public class DataVariable implements Variable {

	private static final long serialVersionUID = -8258439966182439178L;
	private String name = "";
	private Attributes attributes = new Attributes();
	private Long recordIdentifier = null;
	
	public DataVariable(String name, Long recordIdentifier) {
		this.name = name;
		this.recordIdentifier = recordIdentifier;
	}

	public Attributes getVariableAttributes() {
		return attributes;
	}

	public String getVariableName() {
		return name;
	}
	
	public void addAttribute(String attributeName, AttributeType attributeType) {
		attributes.put(attributeName, attributeType);
	}
	
	public void setAttributes(Attributes attributes) {
		if(attributes != null && !attributes.isEmpty()) {
			this.attributes = attributes;
		}
	}
	
	public void addAttributes(Attributes attributes) {
		this.attributes.putAll(attributes);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Variable) {
			Variable variable = (Variable)obj;
			return this.name.equals(variable.getVariableName()) && this.recordIdentifier == variable.getRecordIdentifier();
		}
		return false;
	}

	@Override
	public Long getRecordIdentifier() {
		return recordIdentifier;
	}
}
