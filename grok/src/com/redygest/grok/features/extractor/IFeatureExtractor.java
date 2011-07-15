/**
 * 
 */
package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.grok.features.computation.Features;
import com.redygest.grok.repository.IFeaturesRepository;

/**
 * Interface for feature extractor
 * @author semanticvoid
 *
 */
public interface IFeatureExtractor {

	public Features extract(List<Data> dataList);
		
	public IFeaturesRepository getFeaturesRepository();
	
}
