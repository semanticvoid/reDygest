package com.redygest.grok.prefilter;

/**
 * Prefilter Interface
 * 
 */
public interface IPrefilter {

	/**
	 * Pass (filter) Function
	 * 
	 * @param text
	 * @return true if text passes through filter, false otherwise
	 */
	public boolean pass(String text);

}
