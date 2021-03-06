package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.FeaturesRepository;

public class NPEntityExtractorTest extends TestCase {

	private final IFeatureExtractor pos_extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.POSFEATURE);
	private final IFeatureExtractor npentity_extractor = FeatureExtractorFactory
			.getInstance().getFeatureExtractor(FeatureExtractorType.NPENTITY);

	@Override
	protected void setUp() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		if (FeaturesRepository.getInstance().getFeatureVector(1) == null) {
			Data d1 = new Tweet(
					"{\"text\":\"Lokpal Bill went to Washington DC.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			pos_extractor.extract(dataList, repository);
			npentity_extractor.extract(dataList, repository);
		}
	}

	public void testNPEntity() {
		FeatureVector fv = FeaturesRepository.getInstance().getFeatureVector(1);
		List<IVariable> variables = fv
				.getVariablesWithAttributeType(AttributeId.NPENTITY);
		for (IVariable var : variables) {
			if (var.getVariableName().equals("Lokpal Bill")) {
				assertTrue(true);
				return;
			}
		}

		fail();
	}
}
