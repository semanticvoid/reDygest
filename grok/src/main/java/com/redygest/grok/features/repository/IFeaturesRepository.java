package com.redygest.grok.features.repository;

import java.util.Set;

import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;

public interface IFeaturesRepository {
		
	public void addFeatures(FeatureVectorCollection features);
	
	public FeatureVector getFeatureVector(String recordIdentifier);
	
	public int size();
	
	public Set<Long> getIdentifiers();
	
}
