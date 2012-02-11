package com.redygest.grok.features.datatype;


public class DataVariable implements Variable {

	//private static final long serialVersionUID = -8258439966182439178L;
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
		attributes.put(attributeType, attributeName);
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
			return this.name.equals(variable.getVariableName()) && this.recordIdentifier.equals(variable.getRecordIdentifier());
		}
		return false;
	}

	public Long getRecordIdentifier() {
		return recordIdentifier;
	}

	public int compareTo(Variable arg0) {
		if(equals(arg0)) {
			return 0;
		}
		return 1;
	}
	
	@Override
	public int hashCode() {
	       final int PRIME = 31;
	       int result = 1;
	       result = PRIME * result + name.hashCode() + recordIdentifier.hashCode();	
	       return result;
	}
	
}
