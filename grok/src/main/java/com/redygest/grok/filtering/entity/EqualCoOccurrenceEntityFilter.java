package com.redygest.grok.filtering.entity;

import java.util.List;

import com.redygest.commons.data.Entity;

public class EqualCoOccurrenceEntityFilter implements IEntityFilter {

	public boolean pass(Entity e) {
		if (e.getFrequency() == 0 || e.getCoOccurrences() == null) {
			return false;
		}
		List<Entity> coOccurrences = e.getCoOccurrences();
		for (Entity coOccurEntity : coOccurrences) {
			if (coOccurEntity.getFrequency() != e.getFrequency()) {
				return true;
			}
		}
		return false;
	}

}
