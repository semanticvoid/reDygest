package com.redygest.commons.preprocessor.twitter;

import junit.framework.TestCase;

public class PreprocessorZ20120215Test extends TestCase {

	public void testRepeatedPunctuations() {
		PreprocessorZ20120215 p = new PreprocessorZ20120215();
		String pText = p.removeRepeatedPunctuation("RT @XYZ Awesome!!!!!! seriously???????? really. obviously....");
		assertEquals("RT @XYZ Awesome! seriously? really. obviously.", pText);
	}
	
	public void testRT() {
		PreprocessorZ20120215 p = new PreprocessorZ20120215();
		String pText = p.removeRT("RT @XYZ rt @a_and_not: Awesome!!!!!! seriously???????? really. obviously.... (via @a_and_not)");
		System.out.println("'" + pText + "'");
		assertEquals("  Awesome!!!!!! seriously???????? really. obviously.... ", pText);
	}
	
}
