/**
 * 
 */
package com.redygest.grok.filtering.postextraction.filter;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.grok.classifier.FacOpClassifier;
import com.redygest.grok.filtering.postextraction.IPostExtractionPrefilter;

/**
 * Filter to filter opinions
 * 
 */
public class FactOpinionFilter implements IPostExtractionPrefilter {

	FacOpClassifier classifier = null;

	/**
	 * Constructor
	 */
	public FactOpinionFilter() {
		ConfigReader config = ConfigReader.getInstance();
		classifier = new FacOpClassifier(config.getFacOpModel(),
				config.getFacOpThreshold());
	}

	public boolean pass(Data d) {
		// TODO Auto-generated method stub
		return true;
	}
}
