package com.redygest.grok.features.data.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Attributes {

	private static final long serialVersionUID = 4841911489389335545L;
	private Map<AttributeId, Set<String>> attributeTypeMap = null;

	public Attributes() {
		super();
		attributeTypeMap = new HashMap<AttributeId, Set<String>>();
	}

	public String put(AttributeId key, String value) {
		if (attributeTypeMap.containsKey(key)) {
			attributeTypeMap.get(key).add(value);
		} else {
			Set<String> values = new HashSet<String>();
			values.add(value);
			attributeTypeMap.put(key, values);
		}
		return value;
	}

	public AttributeId remove(AttributeId key) {
		// AttributeType value = super.remove(key);
		if (attributeTypeMap.containsKey(key)) {
			attributeTypeMap.remove(key);
		}
		return key;
	}

	/*
	 * public void putAll(Map<AttributeType, String> m) {
	 * for(Map.Entry<AttributeType, String> entry : m.entrySet()) {
	 * put(entry.getKey(), entry.getValue()); } }
	 */

	public void putAll(Map<AttributeId, Set<String>> m) {
		for (Map.Entry<AttributeId, Set<String>> entry : m.entrySet()) {
			for (String value : entry.getValue()) {
				put(entry.getKey(), value);
			}
		}
	}

	public void putAll(Attributes attributes) {
		putAll(attributes.getAttributesMap());
	}

	public Map<AttributeId, Set<String>> getAttributesMap() {
		return attributeTypeMap;
	}

	public List<String> getAttributeNames(AttributeId type) {
		List<String> names = new ArrayList<String>();

		Set<String> nameSet = attributeTypeMap.get(type);
		if (nameSet != null) {
			names.addAll(nameSet);
		}

		return names;
	}

	public boolean containsAttributeType(AttributeId type) {
		return attributeTypeMap.containsKey(type);
	}

	/*
	 * public AttributeType getAttributeType(String name) { return
	 * super.get(name); }
	 */

	// TODO : implement serialization to db.
	public void serialize() {

	}

	public boolean isEmpty() {
		return attributeTypeMap.isEmpty();
	}

	// TODO : implement deserialization from db.
	public static Attributes deserialize(String serializedObject) {
		return null;
	}
}
