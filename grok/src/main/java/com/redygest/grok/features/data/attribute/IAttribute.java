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
