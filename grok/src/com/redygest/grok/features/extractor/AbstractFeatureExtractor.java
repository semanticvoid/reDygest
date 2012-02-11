package com.redygest.grok.features.extractor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.features.repository.IFeaturesRepository;

public abstract class AbstractFeatureExtractor implements IFeatureExtractor {

	protected static ConfigReader config;

	static {
		config = ConfigReader.getInstance();
	}

	@Override
	public FeatureVectorCollection extract(List<Data> dataList,
			IFeaturesRepository repository) {
		FeatureVectorCollection features = new FeatureVectorCollection();
		Map<Long, FeatureVector> featuresMap = new HashMap<Long, FeatureVector>();
		for (int i = 0; i < dataList.size(); ++i) {
			Data d = dataList.get(i);
			featuresMap.put(
					Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER)),
					extract(d, repository));
		}
		features.addFeatures(featuresMap);
		return features;
	}

	public IFeaturesRepository getFeaturesRepository() {
		return FeaturesRepository.getInstance();
	}

	public static String getCurrentDirPath() {
		try {
			return new java.io.File(".").getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("couldn't get current dir", e);
		}
	}

	/**
	 * populate feature vector for tweet
	 * 
	 * @param t
	 *            - tweet
	 * @return feature vector
	 */
	protected abstract FeatureVector extract(Data t,
			IFeaturesRepository repository);
}
