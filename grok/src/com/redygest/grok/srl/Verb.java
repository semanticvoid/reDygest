package com.redygest.grok.srl;

import java.util.HashMap;
import java.util.List;

public class Verb {

	private int index = -1;
	private int tokenPosition = -1;
	private String text;
	private HashMap<String, List<String>> argumentToText = new HashMap<String, List<String>>();
	private HashMap<String, List<String>> argumentToNPs = new HashMap<String, List<String>>();
	
	/**
	 * Constructor
	 */
	public Verb() {
	}

	/**
	 * Constructor
	 * 
	 * @param text
	 */
	public Verb(String text) {
		this.text = text;
	}
	
	/**
	 * Constructor
	 * @param text
	 */
	public Verb(String text, int index) {
		this.text = text;
		this.index = index;
	}
	
	/**
	 * Constructor
	 * @param text
	 */
	public Verb(String text, int index, int position) {
		this.text = text;
		this.index = index;
		this.tokenPosition = position;
	}

	/**
	 * get text
	 * 
	 * @return
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * set argument to text
	 * 
	 * @param map
	 */
	public void setArgumentToText(HashMap<String, List<String>> map) {
		this.argumentToText = map;
	}

	/**
	 * set argument to NPs
	 * 
	 * @param map
	 */
	public void setArgumentToNPs(HashMap<String, List<String>> map) {
		this.argumentToNPs = map;
	}

	public HashMap<String, List<String>> getArgumentToText() {
		return argumentToText;
	}

	public HashMap<String, List<String>> getArgumentToNPs() {
		return argumentToNPs;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setPosition(int pos) {
		this.tokenPosition = pos;
	}
	
	public int getPosition() {
		return tokenPosition;
	}

}