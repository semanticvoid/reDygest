package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;

public class PunctuationCountFeatureExtractorTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.PUNCTUATIONCOUNTFEATURE);

	private Features f = null;
	
	protected void setUp() {
		if(f == null) {
			Data d1 = new Tweet("{\"text\":\"Oh! yay! Yahoo!\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = extractor.extract(dataList);
		}
	}

	protected void tearDown() {
		// do nothing
	}

	public void testPunctCount() {
		FeatureVector fv = f.getFeature(1);
		Variable var = fv.getVariable(new DataVariable("!", 1L));
		if(var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> tags = attrs.getAttributeNames(AttributeType.PUNCTCOUNT);
			if(tags != null && tags.size() > 0) {
				assertEquals("3", tags.get(0));
				return;
			}
		}
		
		fail();
	}

}
