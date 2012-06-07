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

	public boolean isBoolean() {
		if (getType() != null) {
			return getType() == AttributeType.BOOLEAN;
		}

		return false;
	}

	public boolean isString() {
		if (getType() != null) {
			return getType() == AttributeType.STRING;
		}

		return false;
	}

	public boolean isInteger() {
		if (getType() != null) {
			return getType() == AttributeType.INTEGER;
		}

		return false;
	}

	public boolean isLong() {
		if (getType() != null) {
			return getType() == AttributeType.LONG;
		}

		return false;
	}

	public boolean isDouble() {
		if (getType() != null) {
			return getType() == AttributeType.DOUBLE;
		}

		return false;
	}

	public boolean isFloat() {
		if (getType() != null) {
			return getType() == AttributeType.FLOAT;
		}

		return false;
	}

	public boolean isList() {
		if (getType() != null) {
			return getType() == AttributeType.LIST;
		}

		return false;
	}
}
