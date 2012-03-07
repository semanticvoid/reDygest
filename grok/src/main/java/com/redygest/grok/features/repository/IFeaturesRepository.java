package com.redygest.grok.features.repository;

import java.util.Set;

import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.FeatureVector;

public interface IFeaturesRepository {
		
	public void addFeatures(FeatureVectorCollection features);
	
	public FeatureVector getFeatureVector(String recordIdentifier);
	
	public int size();
	
	public Set<Long> getIdentifiers();
	
}
