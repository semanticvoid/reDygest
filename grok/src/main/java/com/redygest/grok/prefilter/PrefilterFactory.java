/**
 * 
 */
package com.redygest.grok.prefilter;

import com.redygest.grok.prefilter.filter.NonEnglishFilter;
import com.redygest.grok.prefilter.filter.ReplyTweetFilter;

/**
 * Prefilter Factory Class
 * 
 */
public class PrefilterFactory {

	private static PrefilterFactory instance = null;

	/**
	 * private contructor
	 */
	private PrefilterFactory() {
	}

	/**
	 * Factory instance singleton
	 * 
	 * @return
	 */
	public static synchronized PrefilterFactory getInstance() {
		if (instance == null) {
			instance = new PrefilterFactory();
		}

		return instance;
	}

	/**
	 * Produce prefilter function
	 * 
	 * @param type
	 * @return IPrefilter
	 */
	public IPrefilter produce(PrefilterType type) {
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
