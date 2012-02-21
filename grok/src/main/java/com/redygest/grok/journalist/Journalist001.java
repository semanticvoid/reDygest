/**
 * 
 */
package com.redygest.grok.journalist;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Story;
import com.redygest.commons.preprocessor.twitter.PreprocessorZ20120215;
import com.redygest.grok.prefilter.PrefilterRunner;
import com.redygest.grok.prefilter.PrefilterType;

/**
 * Journalist 001
 * (Naming convention 'Journalist 0.0.1')
 * 
 * This is the first version of the production journalist for alpha release
 * 
 */
public class Journalist001 extends BaseJournalist {

	/**
	 * Constructor
	 */
	public Journalist001() {
		super();
		// default twitter preprocessor
		this.preprocessor = new PreprocessorZ20120215();
		// prefilter setup
		this.prefilterRunner = new PrefilterRunner(PrefilterType.NONENLGISH_LANG_FILTER);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.grok.journalist.BaseJournalist#process(java.util.List)
	 */
	@Override
	protected Story process(List<Data> tweets) {
		// TODO
		return null;
	}

}
