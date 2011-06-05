package com.redygest.grok.repository;

import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.FeatureVector;

public class FeaturesRepository implements IFeaturesRepository {
	
	private static FeaturesRepository repository = null;
	
	private Features features = new Features();
	
	public static synchronized FeaturesRepository getInstance() {
		if(repository == null) {
			repository = new FeaturesRepository();
		}
		return repository;
	}
	
	private FeaturesRepository() {
		
	}
	
	public synchronized void addFeatures(Features features) {
		this.features.addFeatures(features.getFeatures());
	}

	@Override
	public FeatureVector getFeature(String recordIdentifier) {
		return features.getFeature(Long.valueOf(recordIdentifier));
	}
}
