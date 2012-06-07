package com.redygest.grok.features.repository;

import java.util.Collection;

import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;

/**
 * Feature Repository Interface
 * 
 */
public interface IFeaturesRepository {

	/**
	 * Add a collection of feature vectors
	 * 
	 * @param features
	 */
	public void addFeatures(FeatureVectorCollection featureVectors);

	/**
	 * Get feature vector for record identifier
	 * 
	 * @param recordIdentifier
	 * @return
	 */
	public FeatureVector getFeatureVector(long recordIdentifier);

	/**
	 * Get repository size
	 * 
	 * @return
	 */
	public int size();

	/**
	 * Get record identifiers in repository
	 * 
	 * @return
	 */
	public Collection<Long> getIdentifiers();

}
