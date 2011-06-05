package com.redygest.grok.repository;

import com.redygest.grok.features.datatype.FeatureVector;

public interface IFeaturesRepository {
		
	FeatureVector getFeature(String recordIdentifier);
	
}
