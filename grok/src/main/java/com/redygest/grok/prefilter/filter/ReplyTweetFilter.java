/**
 * 
 */
package com.redygest.grok.prefilter.filter;

import com.redygest.grok.prefilter.IPrefilter;

/**
 * Filter to filter out reply tweets
 * 
 */
public class ReplyTweetFilter implements IPrefilter {

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
