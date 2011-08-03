package com.redygest.grok.features.extractor;

import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;

public class PPronounCountFeatureExtractor extends AbstractFeatureExtractor {

	String[] ppronouns = { "i", "you", "he", "she", "it", "me", "him", "her",
			"myself", "yourself", "himself", "herself", "itself", "we", "they",
			"us", "them", "mine", "your", "yourself", "our", "ours", "yours" };

	@Override
	public FeatureVector extract(Data t) {
		HashMap<String, Integer> ppCounts = new HashMap<String, Integer>();
		FeatureVector fVector = new FeatureVector();

		List<String> tokens = t.getValues(DataType.BODY_TOKENIZED);

		for (String pp : ppronouns) {
			for (String token : tokens) {
				if (pp.equalsIgnoreCase(token)) {
					if (!ppCounts.containsKey(pp)) {
						ppCounts.put(pp, 0);
					}

					ppCounts.put(pp, ppCounts.get(pp) + 1);
				}
			}
		}

		for (String pp : ppCounts.keySet()) {
			int count = ppCounts.get(pp);
			DataVariable var = new DataVariable(pp, Long.valueOf(t
					.getValue(DataType.RECORD_IDENTIFIER)));
			Attributes attrs = var.getVariableAttributes();
			attrs.put(AttributeType.PPRONOUNCOUNT, String.valueOf(count));
			fVector.addVariable(var);
		}

		return fVector;
	}

}
