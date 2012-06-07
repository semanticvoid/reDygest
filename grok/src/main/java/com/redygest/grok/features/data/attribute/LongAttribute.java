/**
 * 
 */
package com.redygest.grok.features.data.attribute;

/**
 * 
 * Long Attribute
 * 
 * @author semanticvoid
 * 
 */
public class LongAttribute extends AbstractAttribute {

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param value
	 */
	public LongAttribute(AttributeId id, Long value) {
		super(AttributeType.LONG, id, value);
	}

	@Override
	public Long getLong() {
		if (getValue() != null && getValue() instanceof Long) {
			return (Long) getValue();
		}

		return null;
	}

}
