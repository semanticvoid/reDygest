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

import org.apache.commons.lang.StringUtils;

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
		List<String> ret = new ArrayList<String>();
		StringBuffer str = new StringBuffer();
		char prevC;
		for(int i=0; i<this.text.length(); i++) {
			char c = this.text.charAt(i);
			if(punctSet.contains(c)) {
				str.append(' ');
				str.append(c);
				str.append(' ');
			} else {
				str.append(c);
			}
		}
		
		String tokens[] = str.toString().split("[ ]+");
		return new ArrayList<String>(Arrays.asList(tokens));
	}
}