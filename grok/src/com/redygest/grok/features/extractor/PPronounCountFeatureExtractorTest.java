package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
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
		FeatureVector fv = f.getFeature(1);
		Variable var = fv.getVariable(new DataVariable("i", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> tags = attrs
					.getAttributeNames(AttributeType.PPRONOUNCOUNT);
			if (tags != null && tags.size() > 0) {
				assertEquals("2", tags.get(0));
				return;
			}
		}

		fail();
	}

}
