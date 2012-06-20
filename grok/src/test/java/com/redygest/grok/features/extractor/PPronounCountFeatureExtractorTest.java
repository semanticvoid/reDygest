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

public class PPronounCountFeatureExtractorTest extends TestCase {

	private final IFeatureExtractor extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(
					FeatureExtractorType.PPRONOUNCOUNTFEATURE);

	@Override
	protected void setUp() {
		if (FeaturesRepository.getInstance().getFeatureVector(1) == null) {
			Data d1 = new Tweet("{\"text\":\"I am what i am.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			extractor.extract(dataList, FeaturesRepository.getInstance());
		}
	}

	@Override
	protected void tearDown() {
		// do nothing
	}

	public void testPPronounCount() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(1);
		IVariable var = fv.getVariable(new DataVariable("i", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute pronounCountAttr = attrs
					.getAttributes(AttributeId.PPRONOUNCOUNT);
			if (pronounCountAttr != null) {
				assertEquals(2, (long) pronounCountAttr.getLong());
				return;
			}
		}

		fail();
	}

}
