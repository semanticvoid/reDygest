/**
 * 
 */
package com.redygest.grok.filtering.entity;

import com.redygest.commons.data.Entity;

/**
 * Min Length Entity Filter
 * 
 * @author semanticvoid
 * 
 */
public class MinLengthEntityFilter implements IEntityFilter {

	private int length = 2; // default

	/**
	 * Constructor
	 */
	public MinLengthEntityFilter() {
	}

	/**
	 * Constructor
	 */
	public MinLengthEntityFilter(int length) {
		this.length = length;
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
			if (text != null && text.length() <= this.length) {
				return false;
			}
		}

		return true;
	}

}
