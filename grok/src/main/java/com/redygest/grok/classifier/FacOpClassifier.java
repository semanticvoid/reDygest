/**
 * 
 */
package com.redygest.grok.classifier;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.IAttribute;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.features.repository.IFeaturesRepository;

/**
 * Fact Opinion Classifier
 */
public class FacOpClassifier extends VWClassifier {

	public FacOpClassifier(String model, double threshold) {
		super(model, threshold);
	}

	@Override
	protected String getFeatures(Data d) {
		StringBuffer features = new StringBuffer();
		IFeaturesRepository repository = FeaturesRepository.getInstance();
		FeatureVector fVector = repository.getFeatureVector(Long.valueOf(d
				.getValue(DataType.RECORD_IDENTIFIER)));

		// pos bigrams
		features.append("|tagfeatures ");
		List<IVariable> variables = fVector
				.getVariablesWithAttributeType(AttributeId.POSBIGRAMCOUNT);
		for (IVariable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute attr = attrs.getAttributes(AttributeId.POSBIGRAMCOUNT);
			features.append(var.getVariableName().replaceAll(" ", "_") + ":"
					+ attr.getLong() + " ");
		}

		// pos unigrams
		variables = fVector
				.getVariablesWithAttributeType(AttributeId.POSUNIGRAMCOUNT);
		for (IVariable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute attr = attrs.getAttributes(AttributeId.POSUNIGRAMCOUNT);
			features.append(var.getVariableName() + ":" + attr.getLong() + " ");
		}

		// sentiment
		features.append("|sentifeatures ");
		variables = fVector
				.getVariablesWithAttributeType(AttributeId.SENTIMENTCOUNT);
		for (IVariable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute attr = attrs.getAttributes(AttributeId.SENTIMENTCOUNT);
			features.append(var.getVariableName() + ":" + attr.getLong() + " ");
		}

		// adjectives
		features.append("|adjfeatures ");
		int adjCount = 0;
		variables = fVector.getVariablesWithAttributeType(AttributeId.POS);
		for (IVariable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute attr = attrs.getAttributes(AttributeId.POS);
			if (attr.getString().startsWith("JJ")) {
				adjCount += 1;
			}
		}
		features.append("adjs:" + adjCount + " ");

		// pronouns
		features.append("|ppfeatures ");
		variables = fVector
				.getVariablesWithAttributeType(AttributeId.PPRONOUNCOUNT);
		for (IVariable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute attr = attrs.getAttributes(AttributeId.PPRONOUNCOUNT);
			features.append(var.getVariableName() + ":" + attr.getLong() + " ");
		}

		// punctuations
		features.append("|punctfeatures ");
		variables = fVector
				.getVariablesWithAttributeType(AttributeId.PUNCTCOUNT);
		for (IVariable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute attr = attrs.getAttributes(AttributeId.PUNCTCOUNT);
			features.append(var.getVariableName() + ":" + attr.getLong() + " ");
		}

		return features.toString();
	}
}
