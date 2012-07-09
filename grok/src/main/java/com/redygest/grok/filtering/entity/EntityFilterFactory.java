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
		case FREQUENCY_FILTER:
			return new FrequencyEntityFilter();
		case ALPHANUMERIC_FILTER:
			return new AlphaNumericEntityFilter();
		case EQUALCOOCCURRENCE_FILTER:
			return new EqualCoOccurrenceEntityFilter();
		case LENGTH_FILTER:
			return new LengthOfEntityFilter();
		case STOPWORDS_FILTER:
			return new StopwordsEntityFilter();
		default:
			break;
		}

		return null;
	}

	/**
	 * Produce filter function
	 * 
	 * @param type
	 * @return {@link IEntityFilter}
	 */
	public IEntityFilter produce(String type) {
		if (type.equalsIgnoreCase("FREQUENCY_FILTER")) {
			return new FrequencyEntityFilter();
		} else if (type.equalsIgnoreCase("ALPHANUMERIC_FILTER")) {
			return new AlphaNumericEntityFilter();
		} else if (type.equalsIgnoreCase("EQUALCOOCCURRENCE_FILTER")) {
			return new EqualCoOccurrenceEntityFilter();
		} else if (type.equalsIgnoreCase("LENGTH_FILTER")) {
			return new LengthOfEntityFilter();
		} else if (type.equalsIgnoreCase("STOPWORDS_FILTER")) {
			return new StopwordsEntityFilter();
		}
		return null;
	}
}
