package com.redygest.grok.features.data.vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;


public class FeatureVectorCollection {

	public static final long GLOBAL_IDENTIFIER = -1;
	private final Map<Long, FeatureVector> featureVectors = new HashMap<Long, FeatureVector>();

	public Map<Long, FeatureVector> getFeatures() {
		return featureVectors;
	}

	public FeatureVector getFeatureVector(long recordIdentifier) {
		return featureVectors.get(recordIdentifier);
	}

	public void addFeatures(Map<Long, FeatureVector> featureVectors) {
		for (Map.Entry<Long, FeatureVector> entry : featureVectors.entrySet()) {
			if (this.featureVectors.containsKey(entry.getKey())) {
				FeatureVector featureVector = this.featureVectors.get(entry
						.getKey());
				for (IVariable variable : entry.getValue().getVariables()) {
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
			for (IVariable variable : fv.getVariables()) {
				if (overwrite == true
						&& featureVector.getVariable(variable) != null) {
					featureVector.addVariable(variable);
				} else {
					IVariable existingVariable = featureVector
							.getVariable(variable);
					if (existingVariable != null) {
						featureVector.addVariable(merge(variable,
								existingVariable));
					} else {
						featureVector.addVariable(variable);
					}
				}
			}
		} else {
			this.featureVectors.put(GLOBAL_IDENTIFIER, fv);
		}
	}

	private IVariable merge(IVariable newVariable, IVariable existingVariable) {
		Attributes newAttrs = newVariable.getVariableAttributes();
		Attributes existingAttrs = existingVariable.getVariableAttributes();
		Attributes rAttrs = new Attributes();
		rAttrs.putAll(existingAttrs);
		for (AttributeId type : newAttrs.getAttributesMap().keySet()) {
			List<String> newAttributeNames = newAttrs.getAttributeNames(type);
			if (newAttributeNames != null && newAttributeNames.size() > 1) {
				throw new RuntimeException(
						"cannot update a multi valued attribute type in newAttributeNames");
			}
			List<String> existingAttributeNames = existingAttrs
					.getAttributeNames(type);
			if (existingAttributeNames != null
					&& existingAttributeNames.size() > 1) {
				throw new RuntimeException(
						"cannot update a multi valued attribute type in existingAttributeNames");
			}

			if (newAttributeNames != null) {
				String newAttributeName = newAttributeNames.get(0);
				if (existingAttributeNames != null) {
					String existingAttributeName = existingAttributeNames
							.get(0);
					Double rAttributeName = Double.valueOf(newAttributeName)
							+ Double.valueOf(existingAttributeName);
					rAttrs.put(type, Double.toString(rAttributeName));
				} else {
					rAttrs.put(type, newAttributeName);
				}
			}
		}
		DataVariable rVariable = new DataVariable(
				existingVariable.getVariableName(),
				existingVariable.getRecordIdentifier());
		rVariable.addAttributes(rAttrs);
		return rVariable;
	}
}
