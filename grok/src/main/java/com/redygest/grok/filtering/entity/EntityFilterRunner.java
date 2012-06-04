/**
 * 
 */
package com.redygest.grok.filtering.entity;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Entity;

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
	 * @return true if passes all filters, false otherwise
	 */
	public boolean runFilters(List<Entity> entities) {
		List<Entity> filteredEntites = new ArrayList<Entity>();

		for (Entity entity : entities) {
			boolean passed = true;

			for (IEntityFilter filter : filters) {
				if (!filter.pass(entity)) {
					passed = false;
					break;
				}
			}

			if (passed) {
				filteredEntites.add(entity);
			}
		}

		return true;
	}

}
