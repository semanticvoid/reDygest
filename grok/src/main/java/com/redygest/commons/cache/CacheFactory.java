package com.redygest.commons.cache;

import java.util.HashMap;

public class CacheFactory {

	public enum CacheType {
		MEMORY;
	}
	
	private static CacheFactory instance;
	
	private static HashMap<String, ICache> cacheMap = new HashMap<String, ICache>();
	
	private CacheFactory() {
	}
	
	public static CacheFactory getInstance() {
		if(instance == null) {
			instance = new CacheFactory();
		}
		
		return instance;
	}
	
	public ICache produceCache(CacheType type, String name) {
		ICache cache = null;
		String cacheKey = type.toString() + "_" + name;
		
		if(cacheMap.containsKey(cacheKey)) {
			return cacheMap.get(cacheKey);
		}
		
		if(type == CacheType.MEMORY) {
			cache = new InMemoryCache();
		}
		
		cacheMap.put(cacheKey, cache);
		return cache;
	}
}
