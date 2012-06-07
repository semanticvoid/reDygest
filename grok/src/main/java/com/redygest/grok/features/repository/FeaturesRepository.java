package com.redygest.grok.features.repository;

import java.util.Collection;

import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;

/**
 * Feature Repository Class
 * 
 */
public class FeaturesRepository implements IFeaturesRepository {

	// repository instance
	private static FeaturesRepository repository = null;
	// feature vector collection
	private static FeatureVectorCollection features = null;

	/**
	 * Get singleton instance
	 * 
	 * @return
	 */
	public static synchronized FeaturesRepository getInstance() {
		if (repository == null) {
			repository = new FeaturesRepository();
		}
		return repository;
	}

	/**
	 * Private Constructor
	 */
	private FeaturesRepository() {
		features = new FeatureVectorCollection();
	}

	/**
	 * Add features (collection)
	 */
	public synchronized void addFeatures(FeatureVectorCollection features) {
		this.features.addFeatures(features);
	}

	/**
	 * Get feature vector for record id
	 */
	public FeatureVector getFeatureVector(long recordIdentifier) {
		return features.getFeatureVector(recordIdentifier);
	}

	/**
	 * Get size of repository
	 */
	public int size() {
		Collection<FeatureVector> fCol = features.getFeatureVectors();

		if (fCol == null) {
			return 0;
		} else {
			return fCol.size();
		}
	}

	/**
	 * Get record identifiers
	 * 
	 * @return
	 */
	public Collection<Long> getIdentifiers() {
		return this.features.getRecordIdentifiers();
	}

}
