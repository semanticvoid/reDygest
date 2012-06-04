/**
 * 
 */
package com.redygest.grok.filtering.data.postextraction;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;

/**
 * Post Feature Extraction Filter Runner Class
 */
public class PostExtractionPrefilterRunner {

	List<IPostExtractionPrefilter> filters = null;

	/**
	 * Constructor
	 * 
	 */
	public PostExtractionPrefilterRunner(
			PostExtractionPrefilterType... filterTypes) {
		filters = new ArrayList<IPostExtractionPrefilter>();
		PostExtractionPrefilterFactory factory = PostExtractionPrefilterFactory
				.getInstance();
		for (PostExtractionPrefilterType fType : filterTypes) {
			IPostExtractionPrefilter filter = factory.produce(fType);
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
	public boolean runFilters(Data d) {
		for (IPostExtractionPrefilter filter : filters) {
			if (!filter.pass(d)) {
				return false;
			}
		}

		return true;
	}

}
