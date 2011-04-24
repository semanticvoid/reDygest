/**
 * 
 */
package com.redygest.commons.data;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Class representing a Tweet
 * 
 * @author semanticvoid
 * 
 */
public class Tweet {

	private String text;

	/**
	 * Constructor
	 * @param json
	 */
	public Tweet(String json) {
		JSONObject jsonObj = JSONObject.fromObject(json);
		this.text = jsonObj.getString("text");
	}
	
	public String getText() {
		return text;
	}
	
	public String toString() {
		return this.text;
	}
}