package com.redygest.grok.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.redygest.grok.features.extractor.NPCooccurenceFeatureExtractorTest;
import com.redygest.grok.features.extractor.POSFeatureExtractorTest;
import com.redygest.grok.features.extractor.PPronounCountFeatureExtractorTest;
import com.redygest.grok.features.extractor.PunctuationCountFeatureExtractorTest;
import com.redygest.grok.features.extractor.SentimentFeatureExtractorTest;

public class FeatureExtractorTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		// add tests
		suite.addTestSuite(POSFeatureExtractorTest.class);
		suite.addTestSuite(NPCooccurenceFeatureExtractorTest.class);
		suite.addTestSuite(PPronounCountFeatureExtractorTest.class);
		suite.addTestSuite(PunctuationCountFeatureExtractorTest.class);
		suite.addTestSuite(SentimentFeatureExtractorTest.class);
		
		return suite;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	
}
