package com.redygest.commons.nlp;

import com.cybozu.labs.langdetect.LangDetectException;

import junit.framework.TestCase;

public class LanguageDetectorTest extends TestCase {

	public void testDetect() {
		String text = "This is english";
		
		LanguageDetector ld = null;
		
		try {
			ld = new LanguageDetector();
		} catch (LangDetectException e1) {
			e1.printStackTrace();
			fail();
		}
		
		try {
			assertEquals("en", ld.detect(text));
		} catch (LangDetectException e) {
			e.printStackTrace();
			fail();
		}
		
		text = "Esta es una prueba de espa–ol";
		try {
			assertEquals("es", ld.detect(text));
		} catch (LangDetectException e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
