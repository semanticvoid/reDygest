package com.redygest.grok.filtering.postextraction;

import com.redygest.commons.data.Data;

/**
 * Post Feature Extraction Filter Interface
 * 
 */
public interface IPostExtractionPrefilter {

	/**
	 * Pass (filter) Function
	 * 
	 * @param d
	 * @return true if Data passes through filter, false otherwise
	 */
	public boolean pass(Data d);

}
