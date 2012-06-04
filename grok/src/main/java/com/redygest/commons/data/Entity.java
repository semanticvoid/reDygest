/**
 * 
 */
package com.redygest.commons.data;

/**
 * Class representing an Entity
 * 
 * @author semanticvoid
 * 
 */
public class Entity {

	/**
	 * Entity Types
	 * 
	 */
	public enum EntityType {
		NP, NE;
	}

	private String value;
	private EntityType type;

	/**
	 * Constructor
	 * 
	 * @param type
	 * @param value
	 */
	public Entity(EntityType type, String value) {
		setType(type);
		setValue(value);
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public EntityType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(EntityType type) {
		this.type = type;
	}

}
