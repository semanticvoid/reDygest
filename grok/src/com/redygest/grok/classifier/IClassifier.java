package com.redygest.grok.classifier;

import com.redygest.commons.data.Data;

public interface IClassifier {

	/**
	 * Function to train the classifier
	 * @return true on success false otherwise
	 */
	public boolean train();
	
	/**
	 * Function to test the classifier
	 * @return true on success false otherwise
	 */
	public boolean test();
	
	/**
	 * Function to classify a data record
	 * @param d the data
	 * @return the classification result
	 */
	public int classifiy(Data d);
	
}
