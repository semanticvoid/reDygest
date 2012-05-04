/**
 * 
 */
package com.redygest.commons.data;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.Validate;

import com.redygest.commons.nlp.TaggedToken;
import com.redygest.commons.nlp.Tokenizer;
import com.redygest.commons.preprocessor.twitter.ITweetPreprocessor;

/**
 * Class representing a Tweet
 * 
 * @author semanticvoid
 * 
 */
public class Tweet extends AbstractData {

	private String text;
	private String time;
	private String recordIdentifier;

	private final boolean retweeted = false;
	private final int retweetCount = 0;

	private ITweetPreprocessor preprocessor;

	/**
	 * Constructor
	 * 
	 * @param json
	 */
	public Tweet(String json, String recordIdentifier) {
		init(json, recordIdentifier);
	}

	/**
	 * Constructor with preprocessor
	 * 
	 * @param json
	 * @param recordIdentifier
	 * @param preprocessor
	 */
	public Tweet(String json, String recordIdentifier,
			ITweetPreprocessor preprocessor) {
		this.preprocessor = preprocessor;
		init(json, recordIdentifier);
	}

	/**
	 * Function to init the Tweet object
	 * 
	 * @param json
	 * @param recordIdentifier
	 */
	private void init(String json, String recordIdentifier) {
		Validate.notNull(json, "invalid json");
		Validate.notNull(recordIdentifier, "invalid record identifier");
		this.recordIdentifier = recordIdentifier.trim();
		JSONObject jsonObj = null;
		String text = null;
		try {
			jsonObj = JSONObject.fromObject(json);
			text = jsonObj.getString("text");

			if (jsonObj.containsKey("created_at")) {
				time = jsonObj.getString("created_at");
			}

			if (jsonObj.containsKey("retweeted")) {

			}

			if (jsonObj.containsKey("retweet_count")) {

			}
		} catch (Exception e) {
			text = json;
		}
		this.text = text;
		Validate.notEmpty(this.recordIdentifier, "recordIdentifier is empty");
		Validate.notEmpty(this.text, "data is empty");
		populateDataTypes(this.text);
	}

	@Override
	protected boolean isDataPopulated() {
		return true;
	}

	/**
	 * Setter for Preprocessor
	 * 
	 * @param preprocessor
	 */
	public void setPreprocessor(ITweetPreprocessor preprocessor) {
		this.preprocessor = preprocessor;
	}

	/**
	 * Function to tokenize a tweet
	 * 
	 * @param text
	 * @return
	 */
	private List<String> tokenize(String text) {
		Tokenizer tokenizer = Tokenizer.getInstance();
		List<TaggedToken> tokens = tokenizer.tokenize(text);
		List<String> words = new ArrayList<String>();

		for (int i = 0; i < tokens.size(); i++) {
			words.add(tokens.get(i).getWord());
		}

		return words;
	}

	private void populateDataTypes(String text) {
		setValue(DataType.ORIGINAL_TEXT, this.text.trim());
		String tmpText = this.text.trim();
		if (preprocessor != null) {
			tmpText = preprocessor.preprocess(tmpText);
		}
		setValue(DataType.BODY, tmpText);
		setValue(DataType.TIME, time);
		setValues(DataType.BODY_TOKENIZED, tokenize(tmpText));
		setValue(DataType.BODY_PUNCTUATED, tmpText.trim());
		// don't remember what this was for
		setValue(DataType.RECORD_IDENTIFIER, recordIdentifier.trim());

	}

	/**
	 * @return the retweeted
	 */
	public boolean isRetweeted() {
		return retweeted;
	}

	/**
	 * @return the retweetCount
	 */
	public int getRetweetCount() {
		return retweetCount;
	}
}
