/**
 * 
 */
package com.redygest.commons.data;

/**
 * Class representing a Story
 * @author semanticvoid
 *
 */
public class Story {
	
	private int id;
	private String title;
	private String body;
	
	/**
	 * Constructor
	 */
	public Story(String title, String body) {
		this.title = title;
		this.body = body;
		this.id = title.hashCode();
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

}
