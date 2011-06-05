package com.redygest.grok.features.computation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;

public class Features {

	private static final long GLOBAL_IDENTIFIER = -1;
	private Map<Long, FeatureVector> featureVectors = new HashMap<Long, FeatureVector>();

	public Map<Long, FeatureVector> getFeatures() {
		return featureVectors;
	}
	
	public FeatureVector getFeature(long recordIdentifier) {
		return featureVectors.get(recordIdentifier);
	}

	public void addFeatures(Map<Long, FeatureVector> featureVectors) {
		for (Map.Entry<Long, FeatureVector> entry : featureVectors.entrySet()) {
			if (this.featureVectors.containsKey(entry.getKey())) {
				FeatureVector featureVector = this.featureVectors.get(entry
						.getKey());
				for (Variable variable : entry.getValue().getVariables()) {
					featureVector.addVariable(variable);
				}
			} else {
				this.featureVectors.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public void addGlobalFeatures(FeatureVector fv, boolean overwrite) {
		if (this.featureVectors.containsKey(GLOBAL_IDENTIFIER)) {
			FeatureVector featureVector = this.featureVectors
					.get(GLOBAL_IDENTIFIER);
			for (Variable variable : fv.getVariables()) {
				if (overwrite == true
						&& featureVector.getVariable(variable) != null) {
					featureVector.addVariable(variable);
				} else {
					Variable existingVariable = featureVector.getVariable(variable);
					featureVector.addVariable(merge(variable, existingVariable));
				}
			}
		} else {
			this.featureVectors.put(GLOBAL_IDENTIFIER, fv);
		}
	}
	
	private Variable merge(Variable newVariable, Variable existingVariable) {
		Attributes newAttrs = newVariable.getVariableAttributes();
		Attributes existingAttrs = existingVariable.getVariableAttributes();
		Attributes rAttrs = new Attributes();
		rAttrs.putAll(existingAttrs);
		for(AttributeType value : newAttrs.values()) {
				List<String> newAttributeNames = newAttrs.getAttributeNames(value);
				if(newAttributeNames != null && newAttributeNames.size() > 1) {
					throw new RuntimeException("cannot update a multi valued attribute type in newAttributeNames");
				}
				List<String> existingAttributeNames = existingAttrs.getAttributeNames(value);
				if(existingAttributeNames != null && existingAttributeNames.size() > 1) {
					throw new RuntimeException("cannot update a multi valued attribute type in existingAttributeNames");
				}
				
				if(newAttributeNames != null) {
					String newAttributeName = newAttributeNames.get(0);
					if(existingAttributeNames != null) {
						String existingAttributeName = existingAttributeNames.get(0);
						Double rAttributeName = Double.valueOf(newAttributeName) + Double.valueOf(existingAttributeName);
						rAttrs.put(Double.toString(rAttributeName), value);
					} else {
						rAttrs.put(newAttributeName, value);
					}
				}				
		}
		DataVariable rVariable = new DataVariable(existingVariable.getVariableName(), existingVariable.getRecordIdentifier());
		rVariable.addAttributes(rAttrs);
		return rVariable;
	}
}
