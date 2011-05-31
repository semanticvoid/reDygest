package com.redygest.grok.classifier;

import com.redygest.commons.data.Data;

/**
 * Vowpal Wabbit Classifier
 * @author semanticvoid
 *
 */
public class VWClassifier extends AbstractClassifier {

	protected final String EXEC = "/usr/bin/vw";
	
	public int classify(Data d) {
		// TODO yet to implement
		return 0;
	}
}
