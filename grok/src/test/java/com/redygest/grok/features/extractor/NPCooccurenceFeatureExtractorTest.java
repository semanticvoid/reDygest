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

public class NPCooccurenceFeatureExtractorTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.NPCOOCCURRENCE);

	private FeatureVectorCollection f = null;
	
	protected void setUp() {
		if(f == null) {
			Data d1 = new Tweet("{\"text\":\"John hit Mary.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = extractor.extract(dataList, FeaturesRepository.getInstance());
		}
	}

	protected void tearDown() {
		// do nothing
	}

	public void testCooccurenceCount() {
		FeatureVector fv = f.getFeature(FeatureVectorCollection.GLOBAL_IDENTIFIER);
		Variable var = fv.getVariable(new DataVariable("john", FeatureVectorCollection.GLOBAL_IDENTIFIER));
		if(var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> tags = attrs.getAttributeNames(AttributeType.NPCOOCCURENCE);
			if(tags != null && tags.size() > 0) {
				assertEquals("mary", tags.get(0));
				return;
			}
		}
		
		fail();
	}

}
