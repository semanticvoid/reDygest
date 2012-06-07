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

public class PPronounCountFeatureExtractorTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.PPRONOUNCOUNTFEATURE);

	private FeatureVectorCollection f = null;

	protected void setUp() {
		if (f == null) {
			Data d1 = new Tweet("{\"text\":\"I am what i am.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = extractor.extract(dataList, FeaturesRepository.getInstance());
		}
	}

	protected void tearDown() {
		// do nothing
	}

	public void testPPronounCount() {
		FeatureVector fv = f.getFeatureVector(1);
		Variable var = fv.getVariable(new DataVariable("i", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> tags = attrs
					.getAttributeNames(AttributeId.PPRONOUNCOUNT);
			if (tags != null && tags.size() > 0) {
				assertEquals("2", tags.get(0));
				return;
			}
		}

		fail();
	}

}
