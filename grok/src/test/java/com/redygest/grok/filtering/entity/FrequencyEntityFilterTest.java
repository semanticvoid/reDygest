package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Entity;
import com.redygest.commons.data.Entity.EntityType;

import junit.framework.TestCase;

public class FrequencyEntityFilterTest extends TestCase {
	private List<Entity> entities = null;
	IEntityFilter filter = null;
	CoOccurrenceStatistics cs = null;

	@Override
	protected void setUp() {
		entities = new ArrayList<Entity>();
		entities.add(new Entity(EntityType.NE, "a", 2));
		entities.add(new Entity(EntityType.NE, "b", 1));
		entities.add(new Entity(EntityType.NE, "c", 3));
		entities.add(new Entity(EntityType.NE, "d", 4));

		cs = CoOccurrenceStatistics.computeStatistics(entities);
		filter = EntityFilterFactory.getInstance().produce(
				EntityFilterType.FREQUENCY_FILTER);
	}

	public void testFrequencyEntityFilter() {
		for (Entity e : entities) {
			boolean actual = filter.pass(e);
			if (e.getValue().equalsIgnoreCase("a")) {
				assertEquals(true, actual);
			}
			if (e.getValue().equalsIgnoreCase("b")) {
				assertEquals(false, actual);
			}
			if (e.getValue().equalsIgnoreCase("c")) {
				assertEquals(true, actual);
			}
			if (e.getValue().equalsIgnoreCase("d")) {
				assertEquals(true, actual);
			}
		}
	}

}
