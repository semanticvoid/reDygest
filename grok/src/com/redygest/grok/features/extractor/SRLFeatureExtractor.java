/**
 * 
 */
package com.redygest.grok.features.extractor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.knowledge.Event;
import com.redygest.grok.repository.FeaturesRepository;
import com.redygest.grok.repository.IFeaturesRepository;
import com.redygest.grok.srl.Senna;
import com.redygest.grok.srl.Verb;

/**
 * SRL Feature Extractor Class
 */
public class SRLFeatureExtractor extends AbstractFeatureExtractor {

	private static Senna senna = new Senna(config.getSennaPath());

	@Override
	public Features extract(List<Data> dataList) {
		Features features = new Features();
		for (Data t : dataList) {
			features.addGlobalFeatures(extract(t), true);
		}
		return features;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.grok.features.extractor.IFeatureExtractor#extract(com.redygest
	 * .commons.data.Tweet)
	 */
	@Override
	public FeatureVector extract(Data t) {
		FeatureVector fVector = new FeatureVector();
		IFeaturesRepository repository = FeaturesRepository.getInstance();

		String id = t.getValue(DataType.RECORD_IDENTIFIER);

		List<Verb> verbs = senna.getVerbs((t.getValue(DataType.BODY)));

		FeatureVector recordFVector = repository.getFeature(id);
		List<String> tokens = t.getValues(DataType.BODY_TOKENIZED);
		for (String token : tokens) {
			for (Verb verb : verbs) {
				HashMap<String, List<String>> args = verb.getArgumentToText();
				int position = verb.getPosition();

				if (args != null) {

				}
			}
		}
		
		return fVector;
	}

}
