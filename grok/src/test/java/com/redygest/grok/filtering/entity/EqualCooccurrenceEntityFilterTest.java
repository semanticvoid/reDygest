package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Entity;
import com.redygest.commons.data.Entity.EntityType;

public class EqualCooccurrenceEntityFilterTest extends TestCase {
	private List<Entity> entities = null;
	IEntityFilter filter = null;

	@Override
	protected void setUp() {
		filter = EntityFilterFactory.getInstance().produce(
				EntityFilterType.EQUALCOOCCURRENCE_FILTER);
		entities = new ArrayList<Entity>();
		List<Entity> coOccurringEntitites = new ArrayList<Entity>();
		coOccurringEntitites.add(new Entity(EntityType.NE, "b", 1));
		coOccurringEntitites.add(new Entity(EntityType.NE, "c", 1));
		entities.add(new Entity(EntityType.NE, "a", 1, coOccurringEntitites));
	}

	public void testEqualCooccurrenceFilter() {
		for (Entity e : entities) {
			assertEquals(false, filter.pass(e));
		}
	}
}
