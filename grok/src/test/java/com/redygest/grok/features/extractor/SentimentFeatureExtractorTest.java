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

public class SentimentFeatureExtractorTest extends TestCase {

	private final IFeatureExtractor posExtractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.POSFEATURE);
	private final IFeatureExtractor extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(
					FeatureExtractorType.SENTIMENTFEATURE);

	@Override
	protected void setUp() {
		if (FeaturesRepository.getInstance().getFeatureVector(1) == null) {
			Data d1 = new Tweet(
					"{\"text\":\"an abundant supply of water in these lush gardens\"}",
					"1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			FeaturesRepository repository = FeaturesRepository.getInstance();
			repository.addFeatures(posExtractor.extract(dataList, repository));
			extractor.extract(dataList, repository);
		}
	}

	@Override
	protected void tearDown() {
		// do nothing
	}

	public void testSentiment() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(1);
		IVariable var = fv.getVariable(new DataVariable("abundant", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute sentimentAttr = attrs
					.getAttributes(AttributeId.SENTIMENT);
			if (sentimentAttr != null) {
				assertEquals("weak_negative", sentimentAttr.getString());
				return;
			}
		}

		fail();
	}

	public void testSentimentCount() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(1);
		IVariable var = fv.getVariable(new DataVariable("weak_negative", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute sentimentCountAttr = attrs
					.getAttributes(AttributeId.SENTIMENTCOUNT);
			if (sentimentCountAttr != null) {
				assertEquals(2, (long) sentimentCountAttr.getLong());
				return;
			}
		}

		fail();
	}

}
