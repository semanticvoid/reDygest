package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.synonym.ISynonymDb;
import com.redygest.commons.nlp.synonym.SynonymDbFactory;
import com.redygest.commons.nlp.synonym.SynonymDbType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.variable.Variable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class SynonymFeatureExtractor extends AbstractFeatureExtractor {

	ISynonymDb db = null;

	public SynonymFeatureExtractor() {
		db = SynonymDbFactory.getInstance().produce(
				SynonymDbType.NOUNWIKIPEDIAREDIRECT);
	}

	@Override
	protected FeatureVector extract(Data t, IFeaturesRepository repository) {
		FeatureVector fVector = new FeatureVector();

		String id = t.getValue(DataType.RECORD_IDENTIFIER);
		FeatureVector fVector_old = repository.getFeatureVector(id);
		if (fVector_old == null) {
			return fVector;
		}

		List<Variable> variables = new ArrayList<Variable>();
		variables.addAll(fVector_old
				.getVariablesWithAttributeType(AttributeId.NER_CLASS));
		variables.addAll(fVector_old
				.getVariablesWithAttributeType(AttributeId.NPENTITY));

		for (Variable var : variables) {
			String named_entity = var.getVariableName();
			String root = db.getSynonym(named_entity);
			if (root != null) {
				var.addAttribute(root, AttributeId.SYNONYM);
			}
			fVector.addVariable(var);
		}
		return fVector;
	}

}
