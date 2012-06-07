package com.redygest.grok.features.data.attribute;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Attribute Collection
 * 
 */
public class Attributes {

	// attribute map
	private Map<AttributeId, IAttribute> attributeMap = null;

	/**
	 * Constructor
	 */
	public Attributes() {
		super();
		attributeMap = new HashMap<AttributeId, IAttribute>();
	}

	/**
	 * Add attribute
	 * 
	 * @param attr
	 */
	public void add(IAttribute attr) {
		AttributeId id = attr.getId();
		attributeMap.put(id, attr);
	}

	/**
	 * Remove attribute
	 * 
	 * @param id
	 * @return
	 */
	public void remove(AttributeId id) {
		if (attributeMap.containsKey(id)) {
			attributeMap.remove(id);
		}
	}

	/**
	 * Get attribute for Id
	 * 
	 * @param type
	 * @return
	 */
	public IAttribute getAttributes(AttributeId id) {
		return attributeMap.get(id);
	}

	/**
	 * Get all attributes
	 * 
	 * @param type
	 * @return
	 */
	public Collection<IAttribute> getAttributes() {
		return attributeMap.values();
	}

	/**
	 * Check if attribute of id is present
	 * 
	 * @param id
	 * @return
	 */
	public boolean containsAttributeType(AttributeId id) {
		return attributeMap.containsKey(id);
	}

	/**
	 * Check if empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return attributeMap.isEmpty();
	}

}
