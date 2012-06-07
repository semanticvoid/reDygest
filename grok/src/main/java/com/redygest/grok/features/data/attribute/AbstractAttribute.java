/**
 * 
 */
package com.redygest.grok.features.data.attribute;

import java.util.List;

/**
 * Abstract Attribute Class
 * 
 * @author semanticvoid
 * 
 */
public abstract class AbstractAttribute implements IAttribute {

	/**
	 * Atttribute Type Enum
	 * 
	 */
	public enum AttributeType {
		BOOLEAN, INTEGER, STRING, FLOAT, DOUBLE, LONG, LIST;
	}

	// attribute id
	protected AttributeId id;
	// attribute type
	protected AttributeType type;
	// attribute value
	protected Object value;

	/**
	 * Constructor
	 * 
	 * @param type
	 * @param value
	 */
	public AbstractAttribute(AttributeType type, AttributeId id, Object value) {
		this.type = type;
		this.id = id;
		this.value = value;
	}

	public Boolean getBoolean() {
		return null;
	}

	public String getString() {
		return null;
	}

	public Integer getInt() {
		return null;
	}

	public Float getFloat() {
		return null;
	}

	public Long getLong() {
		return null;
	}

	public Double getDouble() {
		return null;
	}

	public List getList() {
		return null;
	}

	public AttributeId getId() {
		return this.id;
	}

	/**
	 * Get value
	 * 
	 * @return
	 */
	protected Object getValue() {
		return this.value;
	}

	/**
	 * Get type
	 * 
	 * @return
	 */
	protected AttributeType getType() {
		return this.type;
	}
}
