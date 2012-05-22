package com.redygest.piggybank.twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONArray;
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
		if (object != null && !object.containsKey("time")) {
			addTime(this.getTime());
		}
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

	public long getTime() {
		long time = -1;
		String timeStr = null;

		if (object.containsKey("created_at")) {
			timeStr = object.getString("created_at");
			// Wed Apr 20 06:14:32 +0000 2011
			SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE MMM d HH:mm:ss Z yyyy");
			try {
				Date d = sdf.parse(timeStr);
				time = d.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return time;
	}

	public JSONArray getTimeArray() {
		JSONArray timeArr = null;

		if (object.containsKey("time")) {
			timeArr = object.getJSONArray("time");
		}

		return timeArr;
	}

	public void setRetweetCount(long count) {
		if (object != null) {
			this.object.remove("retweet_count");
			this.object.accumulate("retweet_count", count);
			this.object.remove("retweeted");
			this.object.accumulate("retweeted", true);
		}
	}

	public void addTime(long time) {
		if (object != null) {
			if (!object.containsKey("time")) {
				object.accumulate("time", new JSONArray());
			}

			JSONArray timeArr = (JSONArray) object.get("time");
			timeArr.add(time);
			object.remove("time");
			object.accumulate("time", timeArr);
		}
	}

	public void addTimeArr(JSONArray time) {
		if (object != null) {
			if (!object.containsKey("time")) {
				object.accumulate("time", new JSONArray());
			}

			JSONArray timeArr = (JSONArray) object.get("time");
			timeArr.addAll(time);
			object.remove("time");
			object.accumulate("time", timeArr);
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
