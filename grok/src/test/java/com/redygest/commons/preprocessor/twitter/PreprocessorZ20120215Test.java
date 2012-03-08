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
		assertEquals("  Awesome!!!!!! seriously???????? really. obviously.... ", pText);
	}
	
	public void testHashTags() {
		PreprocessorZ20120215 p = new PreprocessorZ20120215();
		String pText = p.removeHashTags("RT @MisesNews: BREAKING #NEWS - FORMER #PRESIDENT BILL #CLINTON #HOSPITALIZED in NEW #YORK #CITY http://ow.ly/16wnW");
		assertEquals("RT @MisesNews : BREAKING NEWS - FORMER PRESIDENT BILL CLINTON HOSPITALIZED in NEW YORK CITY http:\\/\\/ow.ly\\/16wnW", pText);
		pText = p.removeHashTags("Bill Clinton, stents, and Demcare: Teachable moment. http://bit.ly/9zYa8g #ksky #txtcot");
		assertEquals("Bill Clinton , stents , and Demcare : Teachable moment . http:\\/\\/bit.ly\\/9zYa8g", pText);
	}
	
	public void testEmoticons() {
		PreprocessorZ20120215 p = new PreprocessorZ20120215();
		String pText = p.removeEmoticons("RT @XYZ rt @a_and_not: Awesome!!!!!! seriously???????? really. :) obviously.... (via @a_and_not)");
		assertEquals("RT @XYZ rt @a_and_not: Awesome!!!!!! seriously???????? really. obviously.... (via @a_and_not)", pText);
	}
}
