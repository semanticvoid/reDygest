package com.redygest.grok.filtering.entity;

import com.redygest.commons.data.Entity;

public class AlphaNumericEntityFilter implements IEntityFilter {

	public boolean pass(Entity e) {
		String text = e.getValue();
		if (text == null) {
			return false;
		}
		String[] split = text.split("[^A-Za-z ]");
		if (split.length > 2) {
			return false;
		}
		return true;
	}

}
