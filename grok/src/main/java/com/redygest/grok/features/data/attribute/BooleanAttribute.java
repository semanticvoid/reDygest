/**
 * 
 */
package com.redygest.grok.features.data.attribute;

/**
 * 
 * Boolean Attribute
 * 
 * @author semanticvoid
 * 
 */
public class BooleanAttribute extends AbstractAttribute {

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param value
	 */
	public BooleanAttribute(AttributeId id, Boolean value) {
		super(AttributeType.BOOLEAN, id, value);
	}

	@Override
	public Boolean getBoolean() {
		if (getValue() != null && getValue() instanceof Boolean) {
			return (Boolean) getValue();
		}

		return null;
	}

}
