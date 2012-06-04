/**
 * 
 */
package com.redygest.grok.filtering.data.preextraction.filter;

import com.redygest.grok.filtering.data.preextraction.IPreExtractionPrefilter;

/**
 * Filter to filter out reply tweets
 * 
 */
public class ReplyTweetFilter implements IPreExtractionPrefilter {

	public ReplyTweetFilter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.grok.prefilter.IPrefilter#pass(java.lang.String)
	 */
	public boolean pass(String text) {
		if (text != null) {
			return !text.startsWith("@");
		}

		return false;
	}

}
