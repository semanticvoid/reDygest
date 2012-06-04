package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.FeaturesRepository;

public class EntityExtractorTest extends TestCase {

	private final IFeatureExtractor posExtractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.POSFEATURE);
	private final IFeatureExtractor npentityExtractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.NPENTITY);
	private final IFeatureExtractor entityExtractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.ENTITY);

	private FeatureVectorCollection f = null;

	@Override
	protected void setUp() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		if (f == null) {
			Data d1 = new Tweet(
					"{\"text\":\"Lokpal Bill went to Washington DC.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = posExtractor.extract(dataList, repository);
			repository.addFeatures(f);
			f = npentityExtractor.extract(dataList, repository);
			repository.addFeatures(f);
			f = entityExtractor.extract(dataList, repository);
			repository.addFeatures(f);
		}
	}

	public void testEntity() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(
				String.valueOf(FeatureVectorCollection.GLOBAL_IDENTIFIER));
		List<Variable> variables = fv
				.getVariablesWithAttributeType(AttributeType.ENTITY);
		for (Variable var : variables) {
			if (var.getVariableName().equals("Lokpal Bill")) {
				assertTrue(true);
				return;
			}
		}

		fail();
	}

	public void testEntityFrequency() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(
				String.valueOf(FeatureVectorCollection.GLOBAL_IDENTIFIER));
		List<Variable> variables = fv
				.getVariablesWithAttributeType(AttributeType.ENTITY);
		for (Variable var : variables) {
			Attributes attrs = var.getVariableAttributes();
			if (attrs != null
					&& attrs.containsAttributeType(AttributeType.FREQUENCY)) {
				int freq = Integer.valueOf(attrs.getAttributeNames(
						AttributeType.FREQUENCY).get(0));
				assertEquals(1, freq);
				return;
			}
		}

		fail();
	}
}
