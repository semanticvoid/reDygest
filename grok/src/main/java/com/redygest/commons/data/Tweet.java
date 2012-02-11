/**
 * 
 */
package com.redygest.commons.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.Validate;

/**
 * Class representing a Tweet
 * 
 * @author semanticvoid
 * 
 */
public class Tweet extends AbstractData {
	
	protected static Set<Character> punctSet;
	
	static {
		Character punctuations[] = { ',', '.', '?', ':', '!', '\'', '"', '[',
				']', '|', '(', ')', '$', '@', '-', ';'};
		punctSet = new HashSet<Character>(Arrays.asList(punctuations));
	}

	final private String text;
	final private String recordIdentifier;

	/**
	 * Constructor
	 * @param json
	 */
	public Tweet(String json, String recordIdentifier) {
		this.recordIdentifier = recordIdentifier;
		JSONObject jsonObj = null;
		String text = null;
		try {
			jsonObj = JSONObject.fromObject(json);
			text = jsonObj.getString("text");
		} catch (Exception e) {
			text = json;
		}
		this.text = text;
		Validate.notEmpty(this.recordIdentifier, "recordIdentifier is empty");
		Validate.notEmpty(this.text, "data is empty");
		populateDataTypeElements(this.text);
	}
	
	@Override
	protected boolean isDataPopulated() {
		return true;
	}
		
	private List<String> tokenize(String text) {
		String tokens[] = text.split("[ ]+");
		return new ArrayList<String>(Arrays.asList(tokens));
	}
	
	private void populateDataTypeElements(String text) {
		//text = StringUtils.lowerCase(text);
		StringBuffer pStr = new StringBuffer();
		StringBuffer str = new StringBuffer();
		for(int i=0; i<text.length(); i++) {
			char c = text.charAt(i);
			if(punctSet.contains(c)) {
				pStr.append(' ');
				pStr.append(c);
				pStr.append(' ');
			} else {
				pStr.append(c);
				str.append(c);
			}
		}
		setValue(DataType.BODY, str.toString().trim());
		setValues(DataType.BODY_TOKENIZED, tokenize(str.toString())); 
		setValue(DataType.BODY_PUNCTUATED, pStr.toString().trim());
		setValue(DataType.RECORD_IDENTIFIER, recordIdentifier.trim());
		setValue(DataType.ORIGINAL_TEXT, this.text.trim());
	}
}
