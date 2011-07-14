package com.redygest.commons.cache;

public interface ICache {

	/**
	 * Cache key-value pair
	 * @param key
	 * @param value
	 * @return true on success false otherwise
	 */
	public boolean put(String key, String value);
	
	/**
	 * Get cached value for key
	 * @param key
	 * @return value or null
	 */
	public String get(String key);
	
	/**
	 * Check for presence of key
	 * @param key
	 * @return true on sucess
	 */
	public boolean hasKey(String key);
}
