package com.redygest.grok.filtering.entity;

import com.redygest.commons.data.Entity;

public class FrequencyEntityFilter implements IEntityFilter {
	public boolean pass(Entity e) {
		long threshold = CoOccurrenceStatistics.frequencyThreshold;
		if (e.getFrequency() < threshold) {
			return false;
		}
		return true;
	}

}
