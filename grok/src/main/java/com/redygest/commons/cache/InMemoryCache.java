package com.redygest.commons.cache;

import java.util.HashMap;

public class InMemoryCache implements ICache {

	private HashMap<String, String> map;
	
	public InMemoryCache() {
		map = new HashMap<String, String>();
	}
	
	public String get(String key) {
		return map.get(key);
	}

	public boolean put(String key, String value) {
		map.put(key, value);
		return true;
	}

	public boolean hasKey(String key) {
		return map.containsKey(key);
	}

}
