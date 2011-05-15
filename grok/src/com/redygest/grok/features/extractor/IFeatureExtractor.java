/**
 * 
 */
package com.redygest.grok.features.extractor;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.datatype.FeatureVector;

/**
 * Interface for feature extractor
 * @author semanticvoid
 *
 */
public interface IFeatureExtractor {

	/**
	 * populate feature vector for tweet
	 * @param t - tweet
	 * @return feature vector
	 */
	public FeatureVector extract(Data t);
	
}
