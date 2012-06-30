package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Entity;
import com.redygest.commons.data.Entity.EntityType;

import junit.framework.TestCase;

public class StopWordEntityFilterTest extends TestCase {
	private List<Entity> entities = null;
	IEntityFilter filter = null;

	@Override
	protected void setUp() {
		entities = new ArrayList<Entity>();
		entities.add(new Entity(EntityType.NE, "rt @ramprakash74", 1));
		entities.add(new Entity(EntityType.NE, "new a", 1));
		filter = EntityFilterFactory.getInstance().produce(
				EntityFilterType.STOPWORDS_FILTER);
	}

	public void testStopWordEntityFilter() {
		for (Entity e : entities) {
			assertEquals(false, filter.pass(e));
		}

	}

}
