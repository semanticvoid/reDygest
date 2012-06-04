/**
 * 
 */
package com.redygest.grok.filtering.data.postextraction;

import com.redygest.grok.filtering.data.postextraction.filter.FactOpinionFilter;

/**
 * Post Feature Extraction Filter Factory Class
 * 
 */
public class PostExtractionPrefilterFactory {

	private static PostExtractionPrefilterFactory instance = null;

	/**
	 * private contructor
	 */
	private PostExtractionPrefilterFactory() {
	}

	/**
	 * Factory instance singleton
	 * 
	 * @return
	 */
	public static synchronized PostExtractionPrefilterFactory getInstance() {
		if (instance == null) {
			instance = new PostExtractionPrefilterFactory();
		}

		return instance;
	}

	/**
	 * Produce prefilter function
	 * 
	 * @param type
	 * @return IPrefilter
	 */
	public IPostExtractionPrefilter produce(PostExtractionPrefilterType type) {
		switch (type) {
		case FACT_OPINION_FILTER:
			return new FactOpinionFilter();

		default:
			break;
		}

		return null;
	}
}
