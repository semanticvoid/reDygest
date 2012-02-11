package com.redygest.commons.data;

/**
 * Named Entity Class
 * 
 */
public class NamedEntity {
	private String nerClass;
	private String text;

	public NamedEntity(String text, String nerClass) {
		this.nerClass = nerClass.trim();
		this.text = text.trim();
	}

	@Override
	public String toString() {
		return this.text + ":" + this.nerClass;
	}

	public String getEntityClass() {
		return nerClass;
	}

	public void setEntityClass(String nerClass) {
		this.nerClass = nerClass;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
