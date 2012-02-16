/**
 * 
 */
package com.redygest.commons.preprocessor.twitter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Preprocessor approach M1
 * 
 */
public class PreprocessorZ20120215 implements ITweetPreprocessor {

	protected static Set<Character> punctSet;

	static {
		Character punctuations[] = { ',', '.', '?', ':', '!', '\'', '"', '[',
				']', '|', '(', ')', '$', '@', '-', ';' };
		punctSet = new HashSet<Character>(Arrays.asList(punctuations));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.commons.preprocessor.twitter.ITweetPreprocessor#preprocess
	 * (java.lang.String)
	 */
	public String preprocess(String text) {

		return null;
	}

	/**
	 * Function to remove repeated punctutation
	 * 
	 * @param text
	 * @return
	 */
	public String removeRepeatedPunctuation(String text) {
		StringBuffer buf = new StringBuffer();
		
		if(text != null) {
			char pChar = 'a';
			boolean flag = false;
			for(int i=0; i<text.length(); i++) {
				char c = text.charAt(i);
				// is punctuation
				if(punctSet.contains(c)) {
					if(flag == true && c != pChar) {
						buf.append(pChar);
					}
					flag = true;
				} else {
					if(flag == true) {
						buf.append(pChar);
					}
					buf.append(c);
					flag = false;
				}
				
				pChar = c;
			}
			
			if(flag == true) {
				buf.append(pChar);
			}
		}
		
		return buf.toString();
	}
}
