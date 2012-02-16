package com.redygest.commons.preprocessor.twitter;

/**
 * Tweet Preprocessor Interface
 *
 */
public interface ITweetPreprocessor {

	/**
	 * Function to preprocess tweet text
	 * @param text
	 * @return	preprocessed text
	 */
	public String preprocess(String text);
	
}
