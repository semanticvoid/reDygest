/**
 * 
 */
package com.redygest.commons.data;

import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * Class representing a Tweet
 * 
 * @author semanticvoid
 * 
 */
public class Tweet extends AbstractData {

	private String text;

	/**
	 * Constructor
	 * @param json
	 */
	public Tweet(String json, String recordIdentifier) {
		JSONObject jsonObj = JSONObject.fromObject(json);
		this.text = jsonObj.getString("text");
		if(!StringUtils.isBlank(this.text)) {
			setValue(DataType.BODY, this.text);
			setValues(DataType.BODY_TOKENIZED, tokenize());
		}
		if(StringUtils.isBlank(recordIdentifier)) {
			throw new RuntimeException("recordIdentifier is empty");
		}
		setValue(DataType.RECORD_IDENTIFIER, recordIdentifier);
	}
	
	@Override
	protected boolean isDataPopulated() {
		return true;
	}
		
	private List<String> tokenize() {
		return Arrays.asList(text.split("[ ,\\t\\n\\r\\f\\.;:\"\'-]+"));
	}
}