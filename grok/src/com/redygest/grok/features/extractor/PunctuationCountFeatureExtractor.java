package com.redygest.grok.features.extractor;

import java.util.HashMap;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;

public class PunctuationCountFeatureExtractor extends AbstractFeatureExtractor {

	char[] puncts = { '.', '!', '?', '@', '#', ',' };

	@Override
	public FeatureVector extract(Data t) {
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
			int count = pCounts.get(c);
			DataVariable var = new DataVariable(c.toString(), Long.valueOf(t
					.getValue(DataType.RECORD_IDENTIFIER)));
			Attributes attrs = var.getVariableAttributes();
			attrs.put(AttributeType.PUNCTCOUNT, String.valueOf(count));
			fVector.addVariable(var);
		}
		
		return fVector;
	}

}
