package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.BooleanAttribute;
import com.redygest.grok.features.data.attribute.IAttribute;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.IFeaturesRepository;

/**
 * Extract NP entities (consecutive NP's)
 * 
 */
public class NPEntityExtractor extends AbstractFeatureExtractor {

	public NPEntityExtractor() {
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

		List<IVariable> variables = fVectorOld
				.getVariablesWithAttributeType(AttributeId.POS);

		StringBuffer entity = new StringBuffer();
		String prevPosTag = null;

		for (IVariable var : variables) {
			String word = var.getVariableName();
			String posTag = null;

			Attributes attrs = var.getVariableAttributes();
			if (attrs != null) {
				IAttribute posAttr = attrs.getAttributes(AttributeId.POS);
				if (posAttr != null) {
					posTag = posAttr.getString();
				}
			}
			if (posTag.startsWith("N") && !posTag.equalsIgnoreCase("NNP")) {
				posTag = "N";
			}

			if ((prevPosTag != null && !posTag.equals(prevPosTag))) {
				if (entity.length() > 0) {
					IVariable eVar = fLocal.getVariable(new DataVariable(entity
							.toString().trim(), id));

					if (eVar == null) {
						eVar = new DataVariable(entity.toString().trim(), id);
					}

					eVar.addAttribute(new BooleanAttribute(
							AttributeId.NPENTITY, true));
					fLocal.addVariable(eVar);

					entity = new StringBuffer();
				}
			}

			if (posTag.startsWith("N")) {
				entity.append(word + " ");
			}

			prevPosTag = posTag;
		}

		if (entity.length() > 0) {
			IVariable eVar = fLocal.getVariable(new DataVariable(entity
					.toString().trim(), Long.valueOf(id)));

			if (eVar == null) {
				eVar = new DataVariable(entity.toString().trim(),
						Long.valueOf(id));
			}

			eVar.addAttribute(new BooleanAttribute(AttributeId.NPENTITY, true));
			fLocal.addVariable(eVar);
		}

		// add feature vector to collection to be returned
		fCollection.put(id, fLocal);

		return fCollection;
	}
}
