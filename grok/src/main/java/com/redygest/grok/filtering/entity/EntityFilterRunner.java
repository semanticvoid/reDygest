/**
 * 
 */
package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.redygest.commons.data.Entity;
import com.redygest.commons.data.EntitySet;

/**
 * Entity Filter Runner Class
 */
public class EntityFilterRunner {

	List<IEntityFilter> filters = null;

	/**
	 * Constructor
	 * 
	 */
	public EntityFilterRunner(EntityFilterType... filterTypes) {
		filters = new ArrayList<IEntityFilter>();
		EntityFilterFactory factory = EntityFilterFactory.getInstance();
		for (EntityFilterType fType : filterTypes) {
			IEntityFilter filter = factory.produce(fType);
			if (filter != null) {
				filters.add(filter);
			}
		}
	}

	/**
	 * Run filters
	 * 
	 * @param d
	 * @return
	 */
	public Set<Entity> runFilters(Set<Entity> entities) {
		Set<Entity> passedEntites = new EntitySet();

		for (Entity entity : entities) {
			boolean passed = true;

			for (IEntityFilter filter : filters) {
				if (!filter.pass(entity)) {
					passed = false;
					break;
				}
			}

			if (passed) {
				passedEntites.add(entity);
			}
		}

		return passedEntites;
	}

}
