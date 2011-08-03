package com.redygest.grok.features.extractor;

import java.io.IOException;
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

public class POSFeatureExtractorTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.POSFEATURE);

	private Features f = null;
	
	protected void setUp() {
		if(f == null) {
			Data d1 = new Tweet("{\"text\":\"This is a test tweet.\"}", "1");
			Data d2 = new Tweet("{\"text\":\"This is what this is.\"}", "2");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			dataList.add(d2);
			f = extractor.extract(dataList);
		}
	}

	protected void tearDown() {
		// do nothing
	}

	public void testPOS() {
		FeatureVector fv = f.getFeature(1);
		Variable var = fv.getVariable(new DataVariable("This", 1L));
		if(var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> tags = attrs.getAttributeNames(AttributeType.POS);
			if(tags != null && tags.size() > 0) {
				assertEquals("DT", tags.get(0));
				return;
			}
		}
		
		fail();
	}
	
	public void testPOSUnigramCount() {
		FeatureVector fv = f.getFeature(1);
		Variable var = fv.getVariable(new DataVariable("DT", 1L));
		if(var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> tags = attrs.getAttributeNames(AttributeType.POSUNIGRAMCOUNT);
			if(tags != null && tags.size() > 0) {
				assertEquals("2", tags.get(0));
				return;
			}
		}
		
		fail();
	}
	
	public void testPOSBigramCount() {
		FeatureVector fv = f.getFeature(2);
		Variable var = fv.getVariable(new DataVariable("DT VBZ", 2L));
		if(var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> tags = attrs.getAttributeNames(AttributeType.POSBIGRAMCOUNT);
			if(tags != null && tags.size() > 0) {
				assertEquals("2", tags.get(0));
				return;
			}	
		}
		fail();
	}	
}
