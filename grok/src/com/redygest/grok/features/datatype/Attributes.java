package com.redygest.grok.features.datatype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Attributes extends HashMap<String, AttributeType> {

	private static final long serialVersionUID = 4841911489389335545L;
	private Map<AttributeType, List<String>> attributeTypeMap = null;
	
	public Attributes() {
		super();
		attributeTypeMap = new HashMap<AttributeType, List<String>>();
	}

	@Override
	public AttributeType put(String key, AttributeType value) {
		super.put(key, value);
		if(attributeTypeMap.containsKey(value)) {
			attributeTypeMap.get(value).add(key);
		} else {
			List<String> keys = new ArrayList<String>();
			keys.add(key);
			attributeTypeMap.put(value, keys);
		}
		return value;
	}
	
	@Override
	public void putAll(Map<? extends String, ? extends AttributeType> m) {
		for(Map.Entry<? extends String, ? extends AttributeType> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}
	
	public List<String> getAttributeNames(AttributeType type) {
		return attributeTypeMap.get(type);
	}
	
	public AttributeType getAttributeType(String name) {
		return super.get(name);
	}
	
	//TODO : implement serialization to db.
	public void serialize() {
		
	}
	
	//TODO : implement deserialization from db.
	public static Attributes deserialize(String serializedObject) {
		return null;
	}
}
