package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.SentiWordNet;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.IAttribute;
import com.redygest.grok.features.data.attribute.LongAttribute;
import com.redygest.grok.features.data.attribute.StringAttribute;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class SentimentFeatureExtractor extends AbstractFeatureExtractor {

	@Override
	public FeatureVectorCollection extract(Data t,
			IFeaturesRepository repository) {
		FeatureVectorCollection fCollection = new FeatureVectorCollection();
		FeatureVector fLocal = new FeatureVector();
		SentiWordNet swn = SentiWordNet.getInstance();

		long id = Long.valueOf(t.getValue(DataType.RECORD_IDENTIFIER));

		if (swn != null) {
			FeatureVector recordFVector = repository.getFeatureVector(id);
			List<String> tokens = t.getValues(DataType.BODY_TOKENIZED);

			for (String token : tokens) {
				IVariable var = recordFVector.getVariable(new DataVariable(
						token, id));
				Attributes attrs = var.getVariableAttributes();

				if (attrs.containsAttributeType(AttributeId.POS)) {
					IAttribute posAttr = attrs.getAttributes(AttributeId.POS);

					if (posAttr != null) {
						String posTag = posAttr.getString();
						String sentiment = swn.extract(token, posTag);

						if (sentiment != null) {
							// sentiment
							DataVariable dataVar = new DataVariable(token, id);
							Attributes attributes = dataVar
									.getVariableAttributes();
							attributes.add(new StringAttribute(
									AttributeId.SENTIMENT, sentiment));
							fLocal.addVariable(dataVar);

							// sentiment count
							var = fLocal.getVariable(new DataVariable(
									sentiment, Long.valueOf(id)));
							if (var == null) {
								var = new DataVariable(sentiment, id);
								attrs = var.getVariableAttributes();
								attrs.add(new LongAttribute(
										AttributeId.SENTIMENTCOUNT, 1L));
							} else {
								attrs = var.getVariableAttributes();
								long count = attrs.getAttributes(
										AttributeId.SENTIMENTCOUNT).getLong();
								count += 1;
								attrs.remove(AttributeId.SENTIMENTCOUNT);
								attrs.add(new LongAttribute(
										AttributeId.SENTIMENTCOUNT, count));
							}

							fLocal.addVariable(var);
						}
					}
				}
			}
		}

		// add feature vector to collection to be returned
		fCollection.put(id, fLocal);

		return fCollection;
	}
}
