package com.redygest.grok.features.data.variable;

import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.IAttribute;

/**
 * Data Variable Class
 */
public class DataVariable implements IVariable {

	// variable name
	private final String name;
	// variable attributes
	private final Attributes attributes;
	// record identifier
	private Long recordIdentifier = null;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param recordIdentifier
	 */
	public DataVariable(String name, Long recordIdentifier) {
		this.name = name;
		this.recordIdentifier = recordIdentifier;
		this.attributes = new Attributes();
	}

	/**
	 * Get Attributes associated with the Variable
	 */
	public Attributes getVariableAttributes() {
		return this.attributes;
	}

	/**
	 * Get Variable name
	 */
	public String getVariableName() {
		return this.name;
	}

	/**
	 * Add attribute
	 * 
	 * @param attr
	 */
	public void addAttribute(IAttribute attr) {
		this.attributes.add(attr);
	}

	/**
	 * Add attributes
	 */
	public void addAttributes(Attributes attrs) {
		if (attrs != null) {
			for (IAttribute attr : attrs.getAttributes()) {
				addAttribute(attr);
			}
		}
	}

	/**
	 * Get record identifier
	 */
	public Long getRecordIdentifier() {
		return recordIdentifier;
	}

	public int compareTo(IVariable arg0) {
		if (equals(arg0)) {
			return 0;
		}
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IVariable) {
			IVariable variable = (IVariable) obj;
			return this.name.equals(variable.getVariableName())
					&& this.recordIdentifier.equals(variable
							.getRecordIdentifier());
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + name.hashCode() + recordIdentifier.hashCode();
		return result;
	}
}
