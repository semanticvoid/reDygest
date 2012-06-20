package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.synonym.ISynonymDb;
import com.redygest.commons.nlp.synonym.SynonymDbFactory;
import com.redygest.commons.nlp.synonym.SynonymDbType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.StringAttribute;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class SynonymFeatureExtractor extends AbstractFeatureExtractor {

	ISynonymDb db = null;

	public SynonymFeatureExtractor() {
		db = SynonymDbFactory.getInstance().produce(
				SynonymDbType.NOUNWIKIPEDIAREDIRECT);
	}

	@Override
	protected FeatureVectorCollection extract(Data t,
			IFeaturesRepository repository) {
		FeatureVectorCollection fCollection = new FeatureVectorCollection();
		FeatureVector fLocal = new FeatureVector();

		long id = Long.valueOf(t.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVectorOld = repository.getFeatureVector(id);
		if (fVectorOld == null) {
			return null;
		}

		List<IVariable> variables = new ArrayList<IVariable>();
		variables.addAll(fVectorOld
				.getVariablesWithAttributeType(AttributeId.NER_CLASS));
		variables.addAll(fVectorOld
				.getVariablesWithAttributeType(AttributeId.NPENTITY));

		for (IVariable var : variables) {
			String named_entity = var.getVariableName();
			String root = db.getSynonym(named_entity);
			if (root != null) {
				var.addAttribute(new StringAttribute(AttributeId.SYNONYM, root));
			}
			fLocal.addVariable(var);
		}

		// add feature vector to collection to be returned
		fCollection.put(id, fLocal);

		return fCollection;
	}
}
