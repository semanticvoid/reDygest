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
	private long frequency;

	/**
	 * Constructor
	 * 
	 * @param type
	 * @param value
	 */
	public Entity(EntityType type, String value) {
		setType(type);
		setValue(value);
		setFrequency(1);
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 * @param value
	 * @param frequncy
	 */
	public Entity(EntityType type, String value, long frequency) {
		setType(type);
		setValue(value);
		setFrequency(frequency);
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

	/**
	 * @return the frequency
	 */
	public long getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	/**
	 * Equals
	 * 
	 * @param e
	 * @return
	 */
	public boolean equals(Entity e) {
		if (e != null) {
			return (this.getValue().equals(e.getValue()) && this.getType() == e
					.getType());
		}

		return false;
	}

}
