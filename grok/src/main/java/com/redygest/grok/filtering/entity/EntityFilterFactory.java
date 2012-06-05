/**
 * 
 */
package com.redygest.grok.filtering.entity;

/**
 * Entity Filter Factory Class
 * 
 */
public class EntityFilterFactory {

	private static EntityFilterFactory instance = null;

	/**
	 * private contructor
	 */
	private EntityFilterFactory() {
	}

	/**
	 * Factory instance singleton
	 * 
	 * @return
	 */
	public static synchronized EntityFilterFactory getInstance() {
		if (instance == null) {
			instance = new EntityFilterFactory();
		}

		return instance;
	}

	/**
	 * Produce filter function
	 * 
	 * @param type
	 * @return {@link IEntityFilter}
	 */
	public IEntityFilter produce(EntityFilterType type) {
		switch (type) {
		case MINLENGTH_FILTER:
			return new MinLengthEntityFilter();

		default:
			break;
		}

		return null;
	}
}
