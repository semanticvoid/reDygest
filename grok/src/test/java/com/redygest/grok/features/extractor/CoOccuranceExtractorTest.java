package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.FeaturesRepository;

import junit.framework.TestCase;

public class CoOccuranceExtractorTest extends TestCase {
	private final IFeatureExtractor posExtractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.POSFEATURE);
	private final IFeatureExtractor npentityExtractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.NPENTITY);
	private final IFeatureExtractor nerentityExtractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.NER);
	private final IFeatureExtractor entityCoOccuranceExtractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(
					FeatureExtractorType.ENTITYCOOCCURANCE);

	private FeatureVectorCollection f = null;

	@Override
	protected void setUp() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		if (f == null) {
			Data d1 = new Tweet(
					"{\"text\":\"Lokpal Bill went to Washington DC.\"}", "1");
			Data d2 = new Tweet("{\"text\":\"Lokpal Bill went to London.\"}",
					"2");
			Data d3 = new Tweet(
					"{\"text\":\"The money went to Clinton.\"}",
					"3");
			Data d4 = new Tweet(
					"{\"text\":\"The money went to Clinton and London.\"}",
					"4");
			Data d5 = new Tweet(
					"{\"text\":\"The America went to Clinton and London.\"}",
					"5");
			List<Data> dataList = new ArrayList<Data>();
			//dataList.add(d1);
			//dataList.add(d2);
			dataList.add(d3);
			dataList.add(d4);
			dataList.add(d5);
			f = posExtractor.extract(dataList, repository);
			repository.addFeatures(f);
			f = npentityExtractor.extract(dataList, repository);
			repository.addFeatures(f);
			f = nerentityExtractor.extract(dataList, repository);
			repository.addFeatures(f);
			f = entityCoOccuranceExtractor.extract(dataList, repository);
			repository.addFeatures(f);
		}
	}

	public void testEntityFrequency() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(
				FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER);
		List<IVariable> variables = fv
				.getVariablesWithAttributeType(AttributeId.COOCCURENCE);
		for (IVariable var : variables) {
			if (var.getVariableName().equalsIgnoreCase("money")) {
				Attributes attrs = var.getVariableAttributes();
				if (attrs != null
						&& attrs
								.containsAttributeType(AttributeId.COOCCURENCE)) {
					List<IVariable> coOccurs = attrs.getAttributes(AttributeId.COOCCURENCE).getList();
					for (IVariable coOccur : coOccurs) {
						if(coOccur.getVariableName().equalsIgnoreCase("London")){
							Long freq =  coOccur.getVariableAttributes().getAttributes(AttributeId.FREQUENCY).getLong();
							assertEquals(1, freq.longValue());
							return;
						}
					}
				}
			}			
		}

		fail();
	}

}
