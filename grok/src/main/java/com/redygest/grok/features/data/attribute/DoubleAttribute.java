/**
 * 
 */
package com.redygest.grok.features.data.attribute;

/**
 * 
 * Double Attribute
 * 
 * @author semanticvoid
 * 
 */
public class DoubleAttribute extends AbstractAttribute {

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param value
	 */
	public DoubleAttribute(AttributeId id, Double value) {
		super(AttributeType.DOUBLE, id, value);
	}

	@Override
	public Double getDouble() {
		if (getValue() != null && getValue() instanceof Double) {
			return (Double) getValue();
		}

		return null;
	}

}
