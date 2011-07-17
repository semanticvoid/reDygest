package com.redygest.grok.srl;

import java.util.HashMap;
import java.util.List;

public class SennaVerb {

	private int index = -1;
	private String text;
	private HashMap<String, List<String>> argumentToText = new HashMap<String, List<String>>();
	private HashMap<String, List<String>> argumentToNPs = new HashMap<String, List<String>>();
	private int startRange = -1;
	private int endRange = -1;
	
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
	 * Constructor
	 * @param text
	 */
	public SennaVerb(String text, int index) {
		this.text = text;
		this.index = index;
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
	
	public void setStartRange(int i) {
		this.startRange = i;
	}
	
	public void setEndRange(int i) {
		this.endRange = i;
	}
	
	public int getStartRange() {
		return this.startRange;
	}
	
	public int getEndRange() {
		return this.endRange;
	}

}