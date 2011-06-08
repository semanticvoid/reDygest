/**
 * 
 */
package com.redygest.grok.classifier;

import com.redygest.commons.data.Data;

/**
 * Fact Opinion Classifier
 */
public class FacOpClassifier extends VWClassifier {

	public FacOpClassifier(String model) {
		super(model);
	}
	
	protected String getFeatures(Data d) {
		// TODO get features
		return "";
	}
	
}
