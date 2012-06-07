package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.SentiWordNet;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class SentimentFeatureExtractor extends AbstractFeatureExtractor {

	@Override
	public FeatureVector extract(Data t, IFeaturesRepository repository) {
		FeatureVector fVector = new FeatureVector();
		SentiWordNet swn = SentiWordNet.getInstance();

		String id = t.getValue(DataType.RECORD_IDENTIFIER);

		if (swn != null) {
			FeatureVector recordFVector = repository.getFeatureVector(id);
			List<String> tokens = t.getValues(DataType.BODY_TOKENIZED);
			for (String token : tokens) {
				IVariable var = recordFVector.getVariable(new DataVariable(
						token, Long.valueOf(id)));
				Attributes attrs = var.getVariableAttributes();
				if (attrs.containsAttributeType(AttributeId.POS)) {
					// TODO why would a given data var have multiple attributes
					// of same type?
					List<String> posTags = attrs
							.getAttributeNames(AttributeId.POS);
					if (posTags.size() > 0) {
						String posTag = posTags.get(0);
						String sentiment = swn.extract(token, posTag);
						if (sentiment != null) {
							// sentiment
							DataVariable dataVar = new DataVariable(token,
									Long.valueOf(id));
							Attributes attributes = dataVar
									.getVariableAttributes();
							attributes.put(AttributeId.SENTIMENT, sentiment);
							fVector.addVariable(dataVar);

							// sentiment count
							var = fVector.getVariable(new DataVariable(
									sentiment, Long.valueOf(id)));
							if (var == null) {
								var = new DataVariable(sentiment,
										Long.valueOf(id));
								attrs = var.getVariableAttributes();
								attrs.put(AttributeId.SENTIMENTCOUNT, "1");
							} else {
								attrs = var.getVariableAttributes();
								int count = Integer.valueOf(attrs
										.getAttributeNames(
												AttributeId.SENTIMENTCOUNT)
										.get(0));
								count += 1;
								attrs.remove(AttributeId.SENTIMENTCOUNT);
								attrs.put(AttributeId.SENTIMENTCOUNT,
										String.valueOf(count));
							}

							fVector.addVariable(var);
						}
					}
				}
			}
		}

		return fVector;
	}

}
