package com.redygest.grok.features.extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.commons.data.Data;
import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.repository.FeaturesRepository;
import com.redygest.grok.repository.IFeaturesRepository;

public abstract class AbstractFeatureExtractor implements IFeatureExtractor{

	@Override
	public Features extract(List<Data> dataList) {
		Features features = new Features();
		Map<Long, FeatureVector> featuresMap = new HashMap<Long, FeatureVector>();
		for(int i = 0; i < dataList.size(); ++i) {
			featuresMap.put((long)i, extract(dataList.get(i)));
		}
		features.addFeatures(featuresMap);
		return features;
	}
	
	public IFeaturesRepository getFeaturesRepository() {
		return FeaturesRepository.getInstance();
	}
	
	/**
	 * populate feature vector for tweet
	 * @param t - tweet
	 * @return feature vector
	 */
	protected abstract FeatureVector extract(Data t);
}
