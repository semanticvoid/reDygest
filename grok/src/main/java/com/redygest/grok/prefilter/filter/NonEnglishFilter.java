/**
 * 
 */
package com.redygest.grok.prefilter.filter;

import com.cybozu.labs.langdetect.LangDetectException;
import com.redygest.commons.nlp.LanguageDetector;
import com.redygest.grok.prefilter.IPrefilter;

/**
 * @author akishore
 *
 */
public class NonEnglishFilter implements IPrefilter {

	LanguageDetector ld = null;
	
	public NonEnglishFilter() {
		try {
			ld = new LanguageDetector();
		} catch (LangDetectException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.redygest.grok.prefilter.IPrefilter#pass(java.lang.String)
	 */
	public boolean pass(String text) {
		if(ld != null) {
			try {
				String lang = ld.detect(text);
				if(!lang.equalsIgnoreCase("en")) {
					return false;
				}
			} catch (LangDetectException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}

}