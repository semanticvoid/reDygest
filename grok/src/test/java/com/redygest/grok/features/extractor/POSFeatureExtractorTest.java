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

public class POSFeatureExtractorTest extends TestCase {

	private final IFeatureExtractor extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.POSFEATURE);

	private FeatureVectorCollection f = null;

	@Override
	protected void setUp() {
		if (f == null) {
			Data d1 = new Tweet("{\"text\":\"This is a test tweet.\"}", "1");
			Data d2 = new Tweet("{\"text\":\"This is what this is.\"}", "2");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			dataList.add(d2);
			f = extractor.extract(dataList, FeaturesRepository.getInstance());
		}
	}

	@Override
	protected void tearDown() {
		// do nothing
	}

	public void testPOS() {
		FeatureVector fv = f.getFeatureVector(1);
		IVariable var = fv.getVariable(new DataVariable("This", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute posFeatureAttribute = attrs
					.getAttributes(AttributeId.POS);
			if (posFeatureAttribute != null) {
				assertEquals("DT", posFeatureAttribute.getString());
				return;
			}
		}

		fail();
	}

	public void testPOSUnigramCount() {
		FeatureVector fv = f.getFeatureVector(1);
		IVariable var = fv.getVariable(new DataVariable("DT", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute posUnigramCountAttribute = attrs
					.getAttributes(AttributeId.POSUNIGRAMCOUNT);
			if (posUnigramCountAttribute != null) {
				assertEquals(2, (long) posUnigramCountAttribute.getLong());
				return;
			}
		}

		fail();
	}

	public void testPOSBigramCount() {
		FeatureVector fv = f.getFeatureVector(2);
		IVariable var = fv.getVariable(new DataVariable("DT VBZ", 2L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			IAttribute posBigramCountAttribute = attrs
					.getAttributes(AttributeId.POSBIGRAMCOUNT);
			if (posBigramCountAttribute != null) {
				assertEquals(2, (long) posBigramCountAttribute.getLong());
				return;
			}
		}

		fail();
	}
}
