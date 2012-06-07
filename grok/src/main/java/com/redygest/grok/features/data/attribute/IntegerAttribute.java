/**
 * 
 */
package com.redygest.grok.features.data.attribute;

/**
 * 
 * Integer Attribute
 * 
 * @author semanticvoid
 * 
 */
public class IntegerAttribute extends AbstractAttribute {

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param value
	 */
	public IntegerAttribute(AttributeId id, Integer value) {
		super(AttributeType.INTEGER, id, value);
	}

	@Override
	public Integer getInt() {
		if (getValue() != null && getValue() instanceof Integer) {
			return (Integer) getValue();
		}

		return null;
	}

}
