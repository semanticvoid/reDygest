package com.redygest.grok.srl;

import java.util.HashMap;
import java.util.List;

public class SennaVerb {

	private String text;
	private HashMap<String, List<String>> argumentToText = new HashMap<String, List<String>>();
	private HashMap<String, List<String>> argumentToNPs = new HashMap<String, List<String>>();

	/**
	 * Constructor
	 */
	public SennaVerb() {
	}

	/**
	 * Constructor
	 * 
	 * @param text
	 */
	public SennaVerb(String text) {
		this.text = text;
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

}