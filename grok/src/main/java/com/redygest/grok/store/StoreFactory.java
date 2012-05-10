/**
 * 
 */
package com.redygest.grok.store;

/**
 * Dygest Store Factory Class
 * 
 * @author semanticvoid
 * 
 */
public class StoreFactory {

	private static StoreFactory instance = null;

	public enum StoreType {
		MYSQL;
	}

	/**
	 * Constructor
	 */
	private StoreFactory() {
	}

	/**
	 * Get instance
	 * 
	 * @return
	 */
	public static synchronized StoreFactory getInstance() {
		if (instance == null) {
			instance = new StoreFactory();
		}

		return instance;
	}

	/**
	 * Function to produce store of type
	 * 
	 * @param type
	 * @return
	 */
	public IStore produce(StoreType type) {
		switch (type) {
		case MYSQL:
			return new DygestMysqlStore();

		default:
			return null;
		}
	}

}
