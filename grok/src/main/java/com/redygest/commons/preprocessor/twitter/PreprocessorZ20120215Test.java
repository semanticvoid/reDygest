package com.redygest.commons.preprocessor.twitter;

import junit.framework.TestCase;

public class PreprocessorZ20120215Test extends TestCase {

	public void testRepreatedPunctuations() {
		PreprocessorZ20120215 p = new PreprocessorZ20120215();
		String pText = p.removeRepeatedPunctuation("RT @XYZ Awesome!!!!!! seriously???????? really. obviously....");
		assertEquals("RT @XYZ Awesome! seriously? really. obviously.", pText);
	}
	
}
