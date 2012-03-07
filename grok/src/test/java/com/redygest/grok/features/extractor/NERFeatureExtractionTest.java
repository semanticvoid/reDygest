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

public class NERFeatureExtractionTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.NER);
	private FeatureVectorCollection f = null;

	protected void setUp() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		if (f == null) {
			Data d1 = new Tweet("{\"text\":\"Obama went to Washington.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = extractor.extract(dataList, repository);
		}
	}

	public void testNER() {
		FeatureVector fv = f.getFeatureVector(1);
		Variable var = fv.getVariable(new DataVariable("Obama", 1L));
		if (var != null) {
			Attributes attrs = var.getVariableAttributes();
			List<String> ner_class = attrs
					.getAttributeNames(AttributeType.NER_CLASS);
			if (ner_class != null && ner_class.size() > 0) {
				assertEquals("PERSON", ner_class.get(0));
				return;
			}
		}

		fail();
	}

}
