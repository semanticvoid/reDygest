package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.Variable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.FeaturesRepository;

public class SynonymFeatureExtractorTest extends TestCase {

	private IFeatureExtractor ner_extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.NER);
	private IFeatureExtractor syn_extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.SYNONYM);

	private FeatureVectorCollection f = null;

	protected void setUp() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		if (f == null) {
			Data d1 = new Tweet("{\"text\":\"Obama went to Washington.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = ner_extractor.extract(dataList, repository);
			repository.addFeatures(f);
			f = syn_extractor.extract(dataList, repository);
			repository.addFeatures(f);
		}
	}

	public void testSynonym() {
		FeatureVector fv = f.getFeatureVector(1);
		Variable var = fv.getVariable(new DataVariable("Obama", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> synonym = attrs
					.getAttributeNames(AttributeId.SYNONYM);
			if (synonym != null && synonym.size() > 0) {
				assertEquals("Barack_Obama", synonym.get(0));
				return;
			}
		}

		fail();
	}

}
