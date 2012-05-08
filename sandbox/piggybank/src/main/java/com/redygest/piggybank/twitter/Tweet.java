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

	public void setRetweetCount(long count) {
		if (object != null) {
			this.object.remove("retweet_count");
			this.object.accumulate("retweet_count", count);
			this.object.remove("retweeted");
			this.object.accumulate("retweeted", true);
		}
	}

	public long getRetweetCount() {
		if (object != null && object.containsKey("retweet_count")) {
			return this.object.getInt("retweet_count");
		}

		return 0;
	}

	public String toJSON() {
		if (object != null) {
			return this.object.toString();
		}

		return null;
	}
}
