package com.redygest.grok.features.computation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;

public abstract class AbstractFeaturesComputation {
	
	private Map<Long, FeatureVector> featureVectors = new HashMap<Long, FeatureVector>();
	
	public List<FeatureVector> getFeatures() {
		return Collections.unmodifiableList(new ArrayList<FeatureVector>(featureVectors.values()));
	}
	
	protected void addFeatures(Map<Long, FeatureVector> featureVectors) {
		for(Map.Entry<Long, FeatureVector> entry : featureVectors.entrySet()) {
			if(this.featureVectors.containsKey(entry.getKey())) {
				FeatureVector featureVector = this.featureVectors.get(entry.getKey());
				for(Variable variable : entry.getValue().getVariables()) {
					featureVector.addVariable(variable);
				}
			} else {
				this.featureVectors.put(entry.getKey(), entry.getValue());
			}
		}
	}
}
