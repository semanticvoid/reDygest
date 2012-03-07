/**
 * 
 */
package com.redygest.grok.classifier;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.features.repository.IFeaturesRepository;

/**
 * Fact Opinion Classifier
 */
public class FacOpClassifier extends VWClassifier {

	public FacOpClassifier(String model, double threshold) {
		super(model, threshold);
	}

	protected String getFeatures(Data d) {
		StringBuffer features = new StringBuffer();
		IFeaturesRepository repository = FeaturesRepository.getInstance();
		FeatureVector fVector = repository.getFeatureVector(d
				.getValue(DataType.RECORD_IDENTIFIER));

		// pos bigrams
		features.append("|tagfeatures ");
		List<Variable> variables = fVector
				.getVariablesWithAttributeType(AttributeType.POSBIGRAMCOUNT);
		for (Variable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			for (String count : attrs
					.getAttributeNames(AttributeType.POSBIGRAMCOUNT)) {
				features.append(var.getVariableName() + ":" + count + " ");
			}
		}

		// pos unigrams
		variables = fVector
				.getVariablesWithAttributeType(AttributeType.POSUNIGRAMCOUNT);
		for (Variable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			for (String count : attrs
					.getAttributeNames(AttributeType.POSUNIGRAMCOUNT)) {
				features.append(var.getVariableName() + ":" + count + " ");
			}
		}

		// sentiment
		features.append("|sentifeatures ");
		variables = fVector
				.getVariablesWithAttributeType(AttributeType.SENTIMENTCOUNT);
		for (Variable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			for (String count : attrs
					.getAttributeNames(AttributeType.SENTIMENTCOUNT)) {
				features.append(var.getVariableName() + ":" + count + " ");
			}
		}
		
		// adjectives
		features.append("|adjfeatures ");
		int adjCount = 0;
		variables = fVector
				.getVariablesWithAttributeType(AttributeType.POS);
		for (Variable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			for (String tag : attrs
					.getAttributeNames(AttributeType.POS)) {
				if(tag.startsWith("JJ")) {
					adjCount += 1;
				}
			}
		}
		features.append("adjs:" + adjCount + " ");
		
		// pronouns
		features.append("|ppfeatures ");
		variables = fVector
				.getVariablesWithAttributeType(AttributeType.PPRONOUNCOUNT);
		for (Variable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			for (String count : attrs
					.getAttributeNames(AttributeType.PPRONOUNCOUNT)) {
				features.append(var.getVariableName() + ":" + count + " ");
			}
		}
		
		// punctuations
		features.append("|punctfeatures ");
		variables = fVector
				.getVariablesWithAttributeType(AttributeType.PUNCTCOUNT);
		for (Variable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			for (String count : attrs
					.getAttributeNames(AttributeType.PUNCTCOUNT)) {
				features.append(var.getVariableName() + ":" + count + " ");
			}
		}

		return features.toString();
	}

}
