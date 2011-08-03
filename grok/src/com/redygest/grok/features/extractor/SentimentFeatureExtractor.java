package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.db.SentiWordNet;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.repository.FeaturesRepository;
import com.redygest.grok.repository.IFeaturesRepository;

public class SentimentFeatureExtractor extends AbstractFeatureExtractor {

	@Override
	public FeatureVector extract(Data t) {
		FeatureVector fVector = new FeatureVector();
		IFeaturesRepository repository = FeaturesRepository.getInstance();
		SentiWordNet swn = SentiWordNet.getInstance();

		String id = t.getValue(DataType.RECORD_IDENTIFIER);

		if (swn != null) {
			FeatureVector recordFVector = repository.getFeature(id);
			List<String> tokens = t.getValues(DataType.BODY_TOKENIZED);
			for (String token : tokens) {
				Variable var = recordFVector.getVariable(new DataVariable(
						token, Long.valueOf(id)));
				Attributes attrs = var.getVariableAttributes();
				if (attrs.containsAttributeType(AttributeType.POS)) {
					// TODO why would a given data var have multiple attributes
					// of same type?
					List<String> posTags = attrs
							.getAttributeNames(AttributeType.POS);
					if (posTags.size() > 0) {
						String posTag = posTags.get(0);
						String sentiment = swn.extract(token, posTag);
						if (sentiment != null) {
							// sentiment
							DataVariable dataVar = new DataVariable(token,
									Long.valueOf(id));
							Attributes attributes = dataVar
									.getVariableAttributes();
							attributes.put(AttributeType.SENTIMENT, sentiment);
							fVector.addVariable(dataVar);

							// sentiment count
							var = fVector.getVariable(new DataVariable(
									sentiment, Long.valueOf(id)));
							if (var == null) {
								var = new DataVariable(sentiment,
										Long.valueOf(id));
								attrs = var.getVariableAttributes();
								attrs.put(AttributeType.SENTIMENTCOUNT, "1");
							} else {
								attrs = var.getVariableAttributes();
								int count = Integer.valueOf(attrs
										.getAttributeNames(
												AttributeType.SENTIMENTCOUNT)
										.get(0));
								count += 1;
								attrs.remove(AttributeType.SENTIMENTCOUNT);
								attrs.put(AttributeType.SENTIMENTCOUNT, String.valueOf(count));
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
