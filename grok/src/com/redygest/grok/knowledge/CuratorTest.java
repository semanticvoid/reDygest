/**
 * 
 */
package com.redygest.grok.knowledge;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.extractor.FeatureExtractorFactory;
import com.redygest.grok.features.extractor.FeatureExtractorType;
import com.redygest.grok.features.extractor.IFeatureExtractor;
import com.redygest.grok.repository.FeaturesRepository;

import junit.framework.TestCase;

/**
 * Unit test cases for Curator
 */
public class CuratorTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.SRL);
	private Features f = null;

	protected void setUp() {
		if(f == null) {
			Data d1 = new Tweet("{\"text\":\"John hit Tom with a bat.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = extractor.extract(dataList);
			FeaturesRepository repository = FeaturesRepository.getInstance();
			repository.addFeatures(f);
		}
	}

	protected void tearDown() {
		// do nothing
	}

	public void testAddRepository() {
		Curator c = new Curator();
		boolean retVal = c.addRepository(FeaturesRepository.getInstance());
		assertEquals(true, retVal);
	}

}
