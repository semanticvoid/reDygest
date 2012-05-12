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
	private Data title = null;
	private List<Data> body = null;

	/**
	 * Constructor
	 */
	public Story(Data title) {
		this.title = title;
		this.body = new ArrayList<Data>();
		this.id = title.hashCode();
	}

	/**
	 * Constructor
	 */
	public Story() {
		this.body = new ArrayList<Data>();
		this.id = new Date().getTime();
	}

	public void addLine(Data line) {
		if (line != null) {
			this.body.add(line);
		}
	}

	public void setTitle(Data title) {
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public Data getTitle() {
		return title;
	}

	public List<Data> getBody() {
		return body;
	}

	public String toJSON() {
		JSONObject jObj = new JSONObject();

		if (title != null) {
			jObj.accumulate("title", title.getValue(DataType.ORIGINAL_TEXT));
		}

		if (body != null) {
			JSONArray body = new JSONArray();
			for (Data d : this.body) {
				JSONObject line = new JSONObject();
				line.accumulate("text", d.getValue(DataType.ORIGINAL_TEXT));
				line.accumulate("time", d.getValue(DataType.TIME));
				body.add(line);
			}
			jObj.accumulate("body", body);
		}

		return jObj.toString();
	}
}
