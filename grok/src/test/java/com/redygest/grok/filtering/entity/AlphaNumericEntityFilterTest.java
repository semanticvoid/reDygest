package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Entity;
import com.redygest.commons.data.Entity.EntityType;

public class AlphaNumericEntityFilterTest extends TestCase {
	private List<Entity> entities = null;
	IEntityFilter filter = null;

	@Override
	protected void setUp() {
		entities = new ArrayList<Entity>();
		entities.add(new Entity(EntityType.NE, "rt@risingindiawins: bjp", 1));
		entities.add(new Entity(EntityType.NE, "@ibnebattuta give'em", 1));
		entities.add(new Entity(EntityType.NE, "papers\\/bills", 1));

		filter = EntityFilterFactory.getInstance().produce(
				EntityFilterType.ALPHANUMERIC_FILTER);
	}

	public void testAlphaNumericEntityFilter() {
		for (Entity e : entities) {
			assertEquals(false, filter.pass(e));
		}
	}

}
