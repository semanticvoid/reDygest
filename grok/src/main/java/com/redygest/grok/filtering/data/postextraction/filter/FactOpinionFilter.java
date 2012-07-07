/**
 * 
 */
package com.redygest.grok.filtering.data.postextraction.filter;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.grok.classifier.FacOpClassifier;
import com.redygest.grok.filtering.data.postextraction.IPostExtractionPrefilter;

/**
 * Filter to filter opinions
 * 
 */
public class FactOpinionFilter implements IPostExtractionPrefilter {

	FacOpClassifier classifier = null;

	/**
	 * Constructor
	 */
	public FactOpinionFilter() {
		ConfigReader config = ConfigReader.getInstance();
		classifier = new FacOpClassifier(config.getFacOpModel(), config
				.getFacOpThreshold());
	}

	/**
	 * Check if Data passes through filter
	 */
	public boolean pass(Data d) {
		List<Data> dataList = new ArrayList<Data>();

		if (d != null) {
			dataList.add(d);
			List<String> classificationResults = classifier.classify(dataList);
			if (classificationResults != null
					&& classificationResults.size() > 0
					&& classificationResults.get(0).equals("1")) {
				return false;
			}
		}

		return true;
	}
}
