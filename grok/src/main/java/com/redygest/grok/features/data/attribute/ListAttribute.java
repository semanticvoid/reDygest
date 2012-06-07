/**
 * 
 */
package com.redygest.grok.features.data.attribute;

import java.util.List;

/**
 * 
 * List Attribute
 * 
 * @author semanticvoid
 * 
 */
public class ListAttribute extends AbstractAttribute {

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param value
	 */
	public ListAttribute(AttributeId id, List value) {
		super(AttributeType.LIST, id, value);
	}

	@Override
	public List getList() {
		if (getValue() != null && getValue() instanceof List) {
			return (List) getValue();
		}

		return null;
	}

}
