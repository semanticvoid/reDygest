package com.redygest.grok.features.extractor;

import java.io.IOException;
import java.util.List;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.features.repository.IFeaturesRepository;

public abstract class AbstractFeatureExtractor implements IFeatureExtractor {

	protected static ConfigReader config;

	static {
		config = ConfigReader.getInstance();
	}

	public FeatureVectorCollection extract(List<Data> dataList,
			IFeaturesRepository repository) {
		FeatureVectorCollection features = new FeatureVectorCollection();

		for (int i = 0; i < dataList.size(); ++i) {
			Data d = dataList.get(i);
			// features.put(Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER)),
			// extract(d, repository));
			FeatureVectorCollection intermediateCollection = extract(d,
					repository);
			// commit to repository
			repository.addFeatures(intermediateCollection);
		}

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
	 * @return feature vector collection
	 */
	protected abstract FeatureVectorCollection extract(Data t,
			IFeaturesRepository repository);
}
