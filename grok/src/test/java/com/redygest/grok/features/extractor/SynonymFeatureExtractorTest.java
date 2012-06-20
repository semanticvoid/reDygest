package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.IAttribute;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.FeaturesRepository;

public class SynonymFeatureExtractorTest extends TestCase {

	private final IFeatureExtractor ner_extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.NER);
	private final IFeatureExtractor syn_extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.SYNONYM);

	@Override
	protected void setUp() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		if (FeaturesRepository.getInstance().getFeatureVector(1) == null) {
			Data d1 = new Tweet("{\"text\":\"Obama went to Washington.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			ner_extractor.extract(dataList, repository);
			syn_extractor.extract(dataList, repository);
		}
	}

	public void testSynonym() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(1);
		IVariable var = fv.getVariable(new DataVariable("Obama", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute synonymAttr = attrs.getAttributes(AttributeId.SYNONYM);
			if (synonymAttr != null) {
				assertEquals("Barack_Obama", synonymAttr.getString());
				return;
			}
		}

		fail();
	}

}
