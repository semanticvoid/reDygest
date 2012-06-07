/**
 * 
 */
package com.redygest.grok.features.extractor;

import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.IFeaturesRepository;
import com.redygest.grok.srl.Senna;
import com.redygest.grok.srl.Verb;

/**
 * SRL Feature Extractor Class
 */
public class SRLFeatureExtractor extends AbstractFeatureExtractor {

	private static Senna senna = new Senna();

	private AttributeId getAttrForLabel(String label) {
		if (label.contains("MNR")) {
			return AttributeId.SRL_MNR;
		} else if (label.contains("LOC")) {
			return AttributeId.SRL_LOC;
		} else if (label.contains("TMP")) {
			return AttributeId.SRL_TMP;
		} else if (label.contains("A0")) {
			return AttributeId.SRL_A0;
		} else if (label.contains("A1")) {
			return AttributeId.SRL_A1;
		} else if (label.contains("A2")) {
			return AttributeId.SRL_A2;
		} else if (label.contains("PNC")) {
			return AttributeId.SRL_PNC;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.grok.features.extractor.IFeatureExtractor#extract(com.redygest
	 * .commons.data.Tweet)
	 */
	@Override
	public FeatureVector extract(Data t, IFeaturesRepository repository) {
		FeatureVector fVector = null;

		String id = t.getValue(DataType.RECORD_IDENTIFIER);
		List<Verb> verbs = senna.getVerbs((t.getValue(DataType.BODY)));
		fVector = repository.getFeatureVector(id);
		if (fVector == null) {
			fVector = new FeatureVector();
		}

		// add semantic role labels as DataVariables for Sentence
		for (Verb v : verbs) {
			String srlId = "SRL_" + v.getIndex();
			IVariable var = fVector.getVariable(new DataVariable(srlId, Long
					.valueOf(id)));
			if (var == null) {
				var = new DataVariable(srlId, Long.valueOf(id));
			}

			Attributes attrs = var.getVariableAttributes();
			HashMap<String, List<String>> args = v.getArgumentToText();
			if (args != null) {
				attrs.put(AttributeId.HAS_SRL, "1");
				attrs.put(AttributeId.SRL_ACTION, v.getText());
				for (String key : args.keySet()) {
					List<String> values = args.get(key);
					for (String value : values) {
						AttributeId type = getAttrForLabel(key);
						if (type != null) {
							attrs.put(type, value);
						}
					}
				}
			}

			fVector.addVariable(var);
		}

		return fVector;
	}

}
