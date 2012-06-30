package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redygest.commons.data.Entity;

public class CoOccurrenceStatistics {
	private List<Entity> entities = null;
	private List<Long> sortedFrequencies = null;
	private static CoOccurrenceStatistics instance = null;
	public static long frequencyThreshold = -1;

	private CoOccurrenceStatistics(List<Entity> entities) {
		this.entities = entities;
		run();
	}

	private void run() {
		frequencyThreshold = computeFrequencyThreshold();
	}

	public static CoOccurrenceStatistics computeStatistics(List<Entity> entities) {
		if (instance == null) {
			instance = new CoOccurrenceStatistics(entities);
		}
		return instance;
	}

	private List<Long> getSortedFrequencies() {
		if (sortedFrequencies != null) {
			return sortedFrequencies;
		}
		List<Long> freqs = new ArrayList<Long>();
		for (Entity e : entities) {
			freqs.add(e.getFrequency());
		}
		Collections.sort(freqs);
		return freqs;
	}

	private long computeFrequencyThreshold() {
		if (frequencyThreshold >= 0) {
			return frequencyThreshold;
		}
		double percentile = 30.0;
		List<Long> sortedFreqs = getSortedFrequencies();
		int ordinal_rank = (int) Math.round((percentile / 100.0)
				* (double) sortedFreqs.size());

		long leastCount = sortedFreqs.get(ordinal_rank);
		return leastCount;
	}

}
