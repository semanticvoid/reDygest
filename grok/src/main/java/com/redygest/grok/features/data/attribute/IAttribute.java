/**
 * 
 */
package com.redygest.grok.features.data.attribute;

import java.util.List;

/**
 * Attribute Interface
 * 
 * @author semanticvoid
 * 
 */
public interface IAttribute {

	/**
	 * Check if Boolean attribute
	 * 
	 * @return
	 */
	public boolean isBoolean();

	/**
	 * Check if String attribute
	 * 
	 * @return
	 */
	public boolean isString();

	/**
	 * Check if Integer attribute
	 * 
	 * @return
	 */
	public boolean isInteger();

	/**
	 * Check if Long attribute
	 * 
	 * @return
	 */
	public boolean isLong();

	/**
	 * Check if Double attribute
	 * 
	 * @return
	 */
	public boolean isDouble();

	/**
	 * Check if Float attribute
	 * 
	 * @return
	 */
	public boolean isFloat();

	/**
	 * Check if List attribute
	 * 
	 * @return
	 */
	public boolean isList();

	/**
	 * Get boolean value
	 * 
	 * @return Boolean
	 */
	public Boolean getBoolean();

	/**
	 * Get String value
	 * 
	 * @return String
	 */
	public String getString();

	/**
	 * Get int value
	 * 
	 * @return int
	 */
	public Integer getInt();

	/**
	 * Get float value
	 * 
	 * @return float
	 */
	public Float getFloat();

	/**
	 * Get long value
	 * 
	 * @return long
	 */
	public Long getLong();

	/**
	 * Get double value
	 * 
	 * @return double
	 */
	public Double getDouble();

	/**
	 * Get list value
	 * 
	 * @return List
	 */
	public List getList();

	/**
	 * Get Id
	 * 
	 * @return {@link AttributeId}
	 */
	public AttributeId getId();

}
