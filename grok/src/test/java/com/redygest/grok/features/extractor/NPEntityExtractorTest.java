package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.FeaturesRepository;

public class NPEntityExtractorTest extends TestCase {

	private final IFeatureExtractor pos_extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.POSFEATURE);
	private final IFeatureExtractor npentity_extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.NPENTITY);

	private FeatureVectorCollection f = null;

	@Override
	protected void setUp() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		if (f == null) {
			Data d1 = new Tweet(
					"{\"text\":\"Lokpal Bill went to Washington DC.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			f = pos_extractor.extract(dataList, repository);
			repository.addFeatures(f);
			f = npentity_extractor.extract(dataList, repository);
			repository.addFeatures(f);
		}
	}

	public void testNPEntity() {
		FeatureVector fv = f.getFeatureVector(1);
		List<Variable> variables = fv
				.getVariablesWithAttributeType(AttributeType.NPENTITY);
		for (Variable var : variables) {
			if (var.getVariableName().equals("Lokpal Bill")) {
				assertTrue(true);
				return;
			}
		}

		fail();
	}
}