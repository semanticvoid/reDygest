/**
 * 
 */
package com.redygest.commons.nlp;

/**
 * Stop Words Class
 */
public class StopWord {

	// TODO move to file & use Map
	public static final String[] ENGLISH_STOP_WORDS = { "a", "an", "and",
			"are", "as", "at", "be", "but", "by", "for", "if", "in", "into",
			"is", "it", "no", "not", "of", "on", "or", "such", "that", "the",
			"their", "then", "there", "these", "they", "this", "to", "was",
			"will", "with" };

	public static boolean isStopWord(String word) {
		if (word != null) {
			for (String s : ENGLISH_STOP_WORDS) {
				if (word.equalsIgnoreCase(s)) {
					return true;
				}
			}
		}

		return false;
	}
}
