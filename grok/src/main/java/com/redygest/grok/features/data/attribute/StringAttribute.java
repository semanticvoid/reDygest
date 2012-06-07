/**
 * 
 */
package com.redygest.grok.features.data.attribute;

/**
 * 
 * String Attribute
 * 
 * @author semanticvoid
 * 
 */
public class StringAttribute extends AbstractAttribute {

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param value
	 */
	public StringAttribute(AttributeId id, String value) {
		super(AttributeType.STRING, id, value);
	}

	@Override
	public String getString() {
		if (getValue() != null && getValue() instanceof String) {
			return (String) getValue();
		}

		return null;
	}

}
