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
	
	public synchronized void addFeatures(FeatureVectorCollection features) {
		this.features.addFeatures(features.getFeatures());
	}

	public FeatureVector getFeatureVector(String recordIdentifier) {
		return features.getFeatureVector(Long.valueOf(recordIdentifier));
	}

	public int size() {
		Map<Long, FeatureVector> fMap = features.getFeatures();
		if(fMap == null) {
			return 0;
		} else {
			return fMap.size();
		}
	}

	public Set<Long> getIdentifiers() {
		Map<Long, FeatureVector> fMap = features.getFeatures();
		if(fMap == null) {
			return null;
		} else {
			return fMap.keySet();
		}
	}
}
