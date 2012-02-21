/**
 * 
 */
package com.redygest.grok.prefilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefilter Runner Class
 */
public class PrefilterRunner {

	List<IPrefilter> filters = null;
	
	/**
	 * Constructor
	 * 
	 */
	public PrefilterRunner(PrefilterType ... filterTypes) {
		filters = new ArrayList<IPrefilter>();
		PrefilterFactory factory = PrefilterFactory.getInstance();
		for(PrefilterType fType : filterTypes) {
			IPrefilter filter = factory.produce(fType);
			if(filter != null) {
				filters.add(filter);
			}
		}
	}
	
	/**
	 * Run prefilters
	 * @param text
	 * @return true if passes all filters, false otherwise
	 */
	public boolean runFilters(String text) {
		for(IPrefilter filter : filters) {
			if(!filter.pass(text)) {
				return false;
			}
		}
		
		return true;
	}

}
