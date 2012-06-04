/**
 * 
 */
package com.redygest.grok.filtering.data.preextraction;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefilter Runner Class
 */
public class PreExtractionPrefilterRunner {

	List<IPreExtractionPrefilter> filters = null;
	
	/**
	 * Constructor
	 * 
	 */
	public PreExtractionPrefilterRunner(PreExtractionPrefilterType ... filterTypes) {
		filters = new ArrayList<IPreExtractionPrefilter>();
		PreExtractionPrefilterFactory factory = PreExtractionPrefilterFactory.getInstance();
		for(PreExtractionPrefilterType fType : filterTypes) {
			IPreExtractionPrefilter filter = factory.produce(fType);
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
		for(IPreExtractionPrefilter filter : filters) {
			if(!filter.pass(text)) {
				return false;
			}
		}
		
		return true;
	}

}
