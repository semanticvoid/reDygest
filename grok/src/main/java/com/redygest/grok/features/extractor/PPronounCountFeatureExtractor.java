package com.redygest.grok.features.extractor;

import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.LongAttribute;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class PPronounCountFeatureExtractor extends AbstractFeatureExtractor {

	String[] ppronouns = { "i", "you", "he", "she", "it", "me", "him", "her",
			"myself", "yourself", "himself", "herself", "itself", "we", "they",
			"us", "them", "mine", "your", "yourself", "our", "ours", "yours" };

	@Override
	public FeatureVectorCollection extract(Data t,
			IFeaturesRepository repository) {
		HashMap<String, Integer> ppCounts = new HashMap<String, Integer>();
		FeatureVectorCollection fCollection = new FeatureVectorCollection();
		FeatureVector fLocal = new FeatureVector();
		long id = Long.valueOf(t.getValue(DataType.RECORD_IDENTIFIER));

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
			DataVariable var = new DataVariable(pp, id);
			Attributes attrs = var.getVariableAttributes();
			attrs.add(new LongAttribute(AttributeId.PPRONOUNCOUNT, (long) count));
			fLocal.addVariable(var);
		}

		// add feature vector to collection to be returned
		fCollection.put(id, fLocal);

		return fCollection;
	}

}
