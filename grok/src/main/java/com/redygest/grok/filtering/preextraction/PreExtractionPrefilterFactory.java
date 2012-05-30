/**
 * 
 */
package com.redygest.grok.filtering.preextraction;

import com.redygest.grok.filtering.preextraction.filter.NonEnglishFilter;
import com.redygest.grok.filtering.preextraction.filter.ReplyTweetFilter;

/**
 * Prefilter Factory Class
 * 
 */
public class PreExtractionPrefilterFactory {

	private static PreExtractionPrefilterFactory instance = null;

	/**
	 * private contructor
	 */
	private PreExtractionPrefilterFactory() {
	}

	/**
	 * Factory instance singleton
	 * 
	 * @return
	 */
	public static synchronized PreExtractionPrefilterFactory getInstance() {
		if (instance == null) {
			instance = new PreExtractionPrefilterFactory();
		}

		return instance;
	}

	/**
	 * Produce prefilter function
	 * 
	 * @param type
	 * @return IPrefilter
	 */
	public IPreExtractionPrefilter produce(PreExtractionPrefilterType type) {
		switch (type) {
		case NONENLGISH_LANG_FILTER:
			return new NonEnglishFilter();
		case REPLY_TWEET_FILTER:
			return new ReplyTweetFilter();

		default:
			break;
		}

		return null;
	}
}
