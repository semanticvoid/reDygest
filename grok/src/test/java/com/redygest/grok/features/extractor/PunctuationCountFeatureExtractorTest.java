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
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.FeaturesRepository;

public class PunctuationCountFeatureExtractorTest extends TestCase {

	private final IFeatureExtractor extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(
					FeatureExtractorType.PUNCTUATIONCOUNTFEATURE);

	private FeatureVectorCollection f = null;

	@Override
	protected void setUp() {
		if (f == null) {
			Data d1 = new Tweet("{\"text\":\"Oh! yay! Yahoo!\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = extractor.extract(dataList, FeaturesRepository.getInstance());
		}
	}

	@Override
	protected void tearDown() {
		// do nothing
	}

	public void testPunctCount() {
		FeatureVector fv = f.getFeatureVector(1);
		IVariable var = fv.getVariable(new DataVariable("!", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute punctCountFeatureAttr = attrs
					.getAttributes(AttributeId.PUNCTCOUNT);
			if (punctCountFeatureAttr != null) {
				assertEquals(3, (long) punctCountFeatureAttr.getLong());
				return;
			}
		}

		fail();
	}

}
