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

public class NERFeatureExtractionTest extends TestCase {

	private final IFeatureExtractor extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.NER);

	@Override
	protected void setUp() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		if (FeaturesRepository.getInstance().getFeatureVector(1) == null) {
			Data d1 = new Tweet(
					"{\"text\":\"Bill Clinton went to Washington.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			extractor.extract(dataList, repository);
		}
	}

	public void testNER() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(1);
		IVariable var = fv.getVariable(new DataVariable("Bill Clinton", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute nerClassFeatureAttr = attrs
					.getAttributes(AttributeId.NER_CLASS);
			if (nerClassFeatureAttr != null) {
				assertEquals("PERSON", nerClassFeatureAttr.getString());
				return;
			}
		}

		fail();
	}

}
