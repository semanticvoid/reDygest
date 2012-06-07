package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.Variable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.FeaturesRepository;

public class SRLFeatureExtractorTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.SRL);

	private FeatureVectorCollection f = null;

	protected void setUp() {
		if (f == null) {
			Data d1 = new Tweet("{\"text\":\"John hit Tom with a bat.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = extractor.extract(dataList, FeaturesRepository.getInstance());
		}
	}

	protected void tearDown() {
		// do nothing
	}

	public void testLabels() {
		FeatureVector fv = f.getFeatureVector(1L);
		List<Variable> variables = fv
				.getVariablesWithAttributeType(AttributeId.SRL_A0);
		if (variables != null) {
			for (Variable v : variables) {
				Attributes attrs = v.getVariableAttributes();
				List<String> tags = attrs
						.getAttributeNames(AttributeId.SRL_A0);
				if (tags != null && tags.size() > 0) {
					assertEquals("John", tags.get(0));
					return;
				}
			}
		}

		fail();
	}

}
