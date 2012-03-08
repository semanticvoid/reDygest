package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.IFeaturesRepository;

/**
 * Extract NP entities (consecutive NP's)
 * 
 */
public class NPEntityExtractor extends AbstractFeatureExtractor {

	public NPEntityExtractor() {
	}

	@Override
	protected FeatureVector extract(Data t, IFeaturesRepository repository) {
		FeatureVector fVector = new FeatureVector();
		String id = t.getValue(DataType.RECORD_IDENTIFIER);

		FeatureVector fVector_old = repository.getFeatureVector(id);
		if (fVector_old == null) {
			return fVector;
		}

		List<Variable> variables = fVector_old
				.getVariablesWithAttributeType(AttributeType.POS);

		StringBuffer entity = new StringBuffer();
		String prevPosTag = null;

		for (Variable var : variables) {
			String word = var.getVariableName();
			String posTag = null;

			Attributes attrs = var.getVariableAttributes();
			if (attrs != null) {
				List<String> names = attrs.getAttributeNames(AttributeType.POS);
				if (names != null) {
					posTag = names.get(0);
				}
			}

			if ((prevPosTag != null && !posTag.equals(prevPosTag))) {
				if (entity.length() > 0) {
					Variable eVar = fVector.getVariable(new DataVariable(entity
							.toString().trim(), Long.valueOf(id)));

					if (eVar == null) {
						eVar = new DataVariable(entity.toString().trim(),
								Long.valueOf(id));
					}

					eVar.addAttribute(prevPosTag, AttributeType.NPENTITY);
					fVector.addVariable(eVar);

					entity = new StringBuffer();
				}
			}

			if (posTag.startsWith("N")) {
				entity.append(word + " ");
			}

			prevPosTag = posTag;
		}

		if (entity.length() > 0) {
			Variable eVar = fVector.getVariable(new DataVariable(entity
					.toString().trim(), Long.valueOf(id)));

			if (eVar == null) {
				eVar = new DataVariable(entity.toString().trim(),
						Long.valueOf(id));
			}

			eVar.addAttribute(prevPosTag, AttributeType.NPENTITY);
			fVector.addVariable(eVar);
		}

		return fVector;
	}
}
