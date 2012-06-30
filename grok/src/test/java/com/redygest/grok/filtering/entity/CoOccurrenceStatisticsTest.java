package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Entity;
import com.redygest.commons.data.Entity.EntityType;

import junit.framework.TestCase;

public class CoOccurrenceStatisticsTest extends TestCase {
	private List<Entity> entities = null;
	CoOccurrenceStatistics cs = null;

	@Override
	protected void setUp() {
		entities = new ArrayList<Entity>();
		entities.add(new Entity(EntityType.NE, "a", 1));
		entities.add(new Entity(EntityType.NE, "b", 4));
		entities.add(new Entity(EntityType.NE, "c", 3));
		entities.add(new Entity(EntityType.NE, "d", 2));
		cs = CoOccurrenceStatistics.computeStatistics(entities);
	}

	public void testSortedFrequencies() {
		long threshold = CoOccurrenceStatistics.frequencyThreshold;
		assertEquals(2, threshold);

	}

}
