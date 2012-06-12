package com.redygest.grok.features.extractor;

import java.util.HashMap;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.LongAttribute;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class PunctuationCountFeatureExtractor extends AbstractFeatureExtractor {

	char[] puncts = { '.', '!', '?', '@', '#', ',' };

	@Override
	public FeatureVector extract(Data t, IFeaturesRepository repository) {
		HashMap<Character, Integer> pCounts = new HashMap<Character, Integer>();
		FeatureVector fVector = new FeatureVector();

		String text = t.getValue(DataType.BODY);
		char[] characters = text.toCharArray();

		for (Character c : characters) {
			for (Character p : puncts) {
				if (c == p) {
					if (!pCounts.containsKey(p)) {
						pCounts.put(p, 0);
					}

					pCounts.put(p, pCounts.get(p) + 1);
				}
			}
		}

		for (Character c : pCounts.keySet()) {
			long count = pCounts.get(c);
			DataVariable var = new DataVariable(c.toString(), Long.valueOf(t
					.getValue(DataType.RECORD_IDENTIFIER)));
			Attributes attrs = var.getVariableAttributes();
			attrs.add(new LongAttribute(AttributeId.PUNCTCOUNT, count));
			fVector.addVariable(var);
		}

		return fVector;
	}
}
