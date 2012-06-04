/**
 * 
 */
package com.redygest.grok.filtering.data.preextraction.filter;

import com.cybozu.labs.langdetect.LangDetectException;
import com.redygest.commons.nlp.LanguageDetector;
import com.redygest.grok.filtering.data.preextraction.IPreExtractionPrefilter;

/**
 * Filter to filter out non-english tweets/data
 * 
 */
public class NonEnglishFilter implements IPreExtractionPrefilter {

	LanguageDetector ld = null;

	public NonEnglishFilter() {
		try {
			ld = new LanguageDetector();
		} catch (LangDetectException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.grok.prefilter.IPrefilter#pass(java.lang.String)
	 */
	public boolean pass(String text) {
		if (ld != null) {
			try {
				String lang = ld.detect(text);
				if (!lang.equalsIgnoreCase("en")) {
					return false;
				}
			} catch (LangDetectException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

}
