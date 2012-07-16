package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Entity;

public class CoOccurrenceStatistics {
	private List<Entity> entities = null;
	private List<Long> sortedFrequencies = null;
	private static CoOccurrenceStatistics instance = null;
	public static long frequencyThreshold = -1;
	public static double percentile = 30.0;

	private CoOccurrenceStatistics(List<Entity> entities) {
		this.entities = entities;
		ConfigReader cr = ConfigReader.getInstance();
		percentile = cr.getPercentileThreshold();
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
		HashSet<Long> unique = new HashSet<Long>(freqs);
		sortedFrequencies = new ArrayList<Long>(unique);
		Collections.sort(sortedFrequencies);
		return sortedFrequencies;
	}

	private long computeFrequencyThreshold() {
		if (frequencyThreshold >= 0) {
			return frequencyThreshold;
		}
		List<Long> sortedFreqs = getSortedFrequencies();
		int ordinal_rank = (int) Math.round((percentile / 100.0)
				* (double) sortedFreqs.size());

		long leastCount = sortedFreqs.get(ordinal_rank);
		return leastCount;
	}

	// TODO caculate the distribution of frequencies
	private void normalizeFrequencies() {

	}

}
