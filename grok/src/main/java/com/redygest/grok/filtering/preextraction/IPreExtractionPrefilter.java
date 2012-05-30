package com.redygest.grok.filtering.preextraction;

/**
 * Prefilter Interface
 * 
 */
public interface IPreExtractionPrefilter {

	/**
	 * Pass (filter) Function
	 * 
	 * @param text
	 * @return true if text passes through filter, false otherwise
	 */
	public boolean pass(String text);

}
