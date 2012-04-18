package com.redygest.piggybank.twitter;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Tweet Class
 * 
 */
public class Tweet {

	private final JSONObject object;

	public Tweet(String jsonStr) throws Exception {
		object = (JSONObject) JSONSerializer.toJSON(jsonStr);
	}

	public String getId() {
		String id = null;
		if (object.containsKey("id")) {
			id = object.getString("id");
		}

		return id;
	}

	public String getText() {
		String txt = null;
		if (object.containsKey("text")) {
			txt = object.getString("text");
		}

		return txt;
	}

}
