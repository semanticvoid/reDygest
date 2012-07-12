package com.redygest.grok.filtering.entity;

import com.redygest.commons.data.Entity;

/**
 * Normalize the frequencies and have a threshold to determine which entities
 * pass. considering the distribution of frequency might give more accurate
 * results than the percentile based method implemented in FrequencyEntityFilter
 */
public class NormalizedFrequencyEntityFilter implements IEntityFilter {

	public boolean pass(Entity e) {
		return false;
	}

}
