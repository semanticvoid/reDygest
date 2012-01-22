package com.redygest.grok.features.repository;

import java.util.Map;
import java.util.Set;

import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.FeatureVector;

public class FeaturesRepository implements IFeaturesRepository {
	
	private static FeaturesRepository repository = null;
	
	private FeatureVectorCollection features = new FeatureVectorCollection();
	
	public static synchronized FeaturesRepository getInstance() {
		if(repository == null) {
			repository = new FeaturesRepository();
		}
		return repository;
	}
	
	private FeaturesRepository() {
		
	}
	
	@Override
	public synchronized void addFeatures(FeatureVectorCollection features) {
		this.features.addFeatures(features.getFeatures());
	}

	@Override
	public FeatureVector getFeature(String recordIdentifier) {
		return features.getFeature(Long.valueOf(recordIdentifier));
	}

	@Override
	public int size() {
		Map<Long, FeatureVector> fMap = features.getFeatures();
		if(fMap == null) {
			return 0;
		} else {
			return fMap.size();
		}
	}

	@Override
	public Set<Long> getIdentifiers() {
		Map<Long, FeatureVector> fMap = features.getFeatures();
		if(fMap == null) {
			return null;
		} else {
			return fMap.keySet();
		}
	}
}
