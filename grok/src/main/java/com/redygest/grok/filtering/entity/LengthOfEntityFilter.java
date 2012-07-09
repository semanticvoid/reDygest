/**
 * 
 */
package com.redygest.grok.filtering.entity;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Entity;

/**
 * Length Entity Filter
 * 
 * @author semanticvoid & tejaswi
 * 
 */
public class LengthOfEntityFilter implements IEntityFilter {

	private int minLength = 3; // default
	private int maxLength = 4;

	/**
	 * Constructor
	 */
	public LengthOfEntityFilter() {
		ConfigReader cr = ConfigReader.getInstance();
		this.minLength = cr.getEntityMinimumLength();
		this.maxLength = cr.getEntityMaximumLength();
	}

	/**
	 * Constructor
	 */
	public LengthOfEntityFilter(int minLength, int maxLength) {
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.grok.filtering.entity.IEntityFilter#pass(com.redygest.commons
	 * .data.Entity)
	 */
	public boolean pass(Entity e) {
		if (e != null) {
			String text = e.getValue();
			if (text == null) {
				return false;
			}
			if (text.length() < this.minLength) {
				return false;
			}
			String[] tokens = text.split("[\\s+.]");
			if (tokens.length > maxLength) {
				return false;
			}
		}

		return true;
	}

}
