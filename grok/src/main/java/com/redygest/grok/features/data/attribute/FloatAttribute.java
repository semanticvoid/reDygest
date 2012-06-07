/**
 * 
 */
package com.redygest.grok.features.data.attribute;

/**
 * 
 * Float Attribute
 * 
 * @author semanticvoid
 * 
 */
public class FloatAttribute extends AbstractAttribute {

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param value
	 */
	public FloatAttribute(AttributeId id, Float value) {
		super(AttributeType.FLOAT, id, value);
	}

	@Override
	public Float getFloat() {
		if (getValue() != null && getValue() instanceof Float) {
			return (Float) getValue();
		}

		return null;
	}

}
