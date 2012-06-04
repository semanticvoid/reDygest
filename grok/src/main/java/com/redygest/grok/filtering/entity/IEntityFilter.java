package com.redygest.grok.filtering.entity;

import com.redygest.commons.data.Entity;

/**
 * Entity Filter Interface
 * 
 */
public interface IEntityFilter {

	/**
	 * Pass (filter) Function
	 * 
	 * @param e
	 * @return true if Data passes through filter, false otherwise
	 */
	public boolean pass(Entity e);

}
