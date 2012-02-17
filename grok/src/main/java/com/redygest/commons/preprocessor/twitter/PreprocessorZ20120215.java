/**
 * 
 */
package com.redygest.commons.preprocessor.twitter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redygest.commons.nlp.POSTagger;
import com.redygest.commons.nlp.TaggedToken;

/**
 * Preprocessor approach Z20120215
 * 
 */
public class PreprocessorZ20120215 implements ITweetPreprocessor {

	protected static Set<Character> punctSet;
	protected static Set<String> emotiSet;

	static {
		Character punctuations[] = { ',', '.', '?', ':', '!', '\'', '"', '[',
				']', '|', '(', ')', '$', '@', '-', ';', ' ' };
		punctSet = new HashSet<Character>(Arrays.asList(punctuations));
	}

	static {
		String emoticons[] = { ":-)", ":)", ":o)", ":]", "=)", ":-D", ":D",
				"8D", "xD", "=D", "8-)", ":-))", ":-(", ":(", ":-<", ":<",
				":-[", ":[", ":{", ";-)", ";)", ";-]", ";]", ":-P", ":P",
				":-p", ":p", ":-O", ":O", ":-|", ";P", ";-P", ";p", ";-p" };
		emotiSet = new HashSet<String>(Arrays.asList(emoticons));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.commons.preprocessor.twitter.ITweetPreprocessor#preprocess
	 * (java.lang.String)
	 */
	public String preprocess(String text) {
		String preprocessedText = removeRT(text);
		preprocessedText = removeRepeatedPunctuation(preprocessedText);
		preprocessedText = removeEmoticons(preprocessedText);
		preprocessedText = removeHashTags(preprocessedText);
		return preprocessedText;
	}

	/**
	 * Function to remove RT's
	 * 
	 * @param text
	 * @return
	 */
	public String removeRT(String text) {
		String temp = text.replaceAll("(?i)RT[ ]+@[\\w\\d\\-:]+", "");
		return temp.replaceAll("(?i)[(]via[ ]+@[\\w\\d\\-:]+[)]", "");
	}

	/**
	 * Function to remove repeated punctutation
	 * 
	 * @param text
	 * @return
	 */
	public String removeRepeatedPunctuation(String text) {
		StringBuffer buf = new StringBuffer();

		// TODO handle http://
		if (text != null) {
			char pChar = 'a';
			boolean flag = false;
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				// is punctuation
				if (punctSet.contains(c)) {
					if (flag == true && c != pChar) {
						buf.append(pChar);
					}
					flag = true;
				} else {
					if (flag == true) {
						buf.append(pChar);
					}
					buf.append(c);
					flag = false;
				}

				pChar = c;
			}

			if (flag == true) {
				buf.append(pChar);
			}
		}

		return buf.toString();
	}
	

	/**
	 * Function to remove/process hashtags
	 * 
	 * @param text
	 * @return
	 */
	public String removeHashTags(String text) {
		POSTagger tagger = POSTagger.getInstance();
		List<TaggedToken> tokens = tagger.tag(text);
		int size = tokens.size();
		int midStart = (int) (0.25 * size);
		int midEnd = (int) (0.75 * size);

		boolean lastFlag = true;
		for (int i = size - 1; i >= 0; i--) {
			TaggedToken token = tokens.get(i);
			// if *still* last token and is hashtag
			if (lastFlag && token.getWord().startsWith("#")) {
				if ((i - 1) >= 0) {
					TaggedToken pToken = tokens.get((i - 1));
					if (pToken.getWord().startsWith("#")
							|| pToken.getPosTag().equals(".")) {
						tokens.remove(i);
					} else if (pToken.getPosTag().equals("CC")
							|| pToken.getPosTag().equals("IN")) {
						lastFlag = false;
					} else {
						tokens.remove(i);
					}
				}
			} else {
				lastFlag = false;
				if (token.getWord().startsWith("#")) {
					token.setWord(token.getWord().replaceAll("^#", ""));
				}
			}
		}

		// form the text back
		StringBuffer buf = new StringBuffer();
		for (TaggedToken token : tokens) {
			buf.append(token.getWord() + " ");
		}

		return buf.toString().trim();
	}
	
	/**
	 * Remove emoticons
	 * @param text
	 * @return
	 */
	public String removeEmoticons(String text) {
		StringBuffer buf = new StringBuffer();

		if (text != null) {
			String[] tokens = text.split("[ ]+");
			for(String token : tokens) {
				if(!emotiSet.contains(token)) {
					buf.append(token + " ");
				}
			}
		}

		return buf.toString().trim();
	}
}
