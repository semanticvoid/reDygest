package com.redygest.grok.features.computation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;

public abstract class AbstractFeaturesComputation {

	private static final long GLOBAL_IDENTIFIER = -1;
	private Map<Long, FeatureVector> featureVectors = new HashMap<Long, FeatureVector>();

	public List<FeatureVector> getFeatures() {
		return Collections.unmodifiableList(new ArrayList<FeatureVector>(
				featureVectors.values()));
	}

	protected void addFeatures(Map<Long, FeatureVector> featureVectors) {
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

	protected void addGlobalFeatures(FeatureVector fv, boolean overwrite) {
		if (this.featureVectors.containsKey(GLOBAL_IDENTIFIER)) {
			FeatureVector featureVector = this.featureVectors
					.get(GLOBAL_IDENTIFIER);
			for (Variable variable : fv.getVariables()) {
				if (overwrite == true
						&& featureVector.getVariable(variable) != null) {
					featureVector.addVariable(variable);
				} else {
					// TODO merge
				}
			}
		} else {
			this.featureVectors.put(GLOBAL_IDENTIFIER, fv);
		}
	}
}
