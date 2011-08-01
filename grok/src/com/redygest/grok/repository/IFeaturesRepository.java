package com.redygest.grok.repository;

import java.util.Set;

import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.FeatureVector;

public interface IFeaturesRepository {
		
	public void addFeatures(Features features);
	
	public FeatureVector getFeature(String recordIdentifier);
	
	public int size();
	
	public Set<Long> getIdentifiers();
	
}
