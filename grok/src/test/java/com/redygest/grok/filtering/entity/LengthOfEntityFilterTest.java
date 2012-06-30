package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Entity;
import com.redygest.commons.data.Entity.EntityType;

import junit.framework.TestCase;

public class LengthOfEntityFilterTest extends TestCase {
	private List<Entity> entities = null;
	IEntityFilter filter = null;

	@Override
	protected void setUp() {
		entities = new ArrayList<Entity>();
		entities.add(new Entity(EntityType.NE, "p", 1));
		entities.add(new Entity(EntityType.NE, "pa", 1));
		entities.add(new Entity(EntityType.NE, "pa ap pa ap pa", 1));

		filter = EntityFilterFactory.getInstance().produce(
				EntityFilterType.LENGTH_FILTER);
	}

	public void testLengthOfEntityFilter() {
		for (Entity e : entities) {
			assertEquals(false, filter.pass(e));
		}
	}

}
