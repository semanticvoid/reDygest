/**
 * 
 */
package com.redygest.commons.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Class representing a Story
 * 
 * @author semanticvoid
 * 
 */
public class Story {

	private final long id;
	private String title = null;
	private List<String> body = null;

	/**
	 * Constructor
	 */
	public Story(String title) {
		this.title = title;
		this.body = new ArrayList<String>();
		this.id = title.hashCode();
	}

	/**
	 * Constructor
	 */
	public Story() {
		this.body = new ArrayList<String>();
		this.id = new Date().getTime();
	}

	public void addLine(String line) {
		if (line != null) {
			this.body.add(line);
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getBody() {
		return body;
	}

	public String toJSON() {
		JSONObject jObj = new JSONObject();

		if (title != null) {
			jObj.accumulate("title", title);
		}

		if (body != null) {
			JSONArray body = new JSONArray();
			for (String line : this.body) {
				body.add(line);
			}
			jObj.accumulate("body", body);
		}

		return jObj.toString();
	}
}
