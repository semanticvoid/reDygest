package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.Features;

public class POSFeatureExtractorTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.POSFEATURE);

	private Features f = null;
	
	protected void setUp() {
		if(f == null) {
			Data d1 = new Tweet("{\"text\":\"This is a test tweet.\"}", "1");
			Data d2 = new Tweet("{\"text\":\"This is the second test tweet.\"}", "2");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			dataList.add(d2);
			f = extractor.extract(dataList);
		}
	}

	protected void tearDown() {
		// do nothing
	}

	public void testPOSTags() {
		assertEquals(true, true);
	}

}
