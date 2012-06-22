package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.BooleanAttribute;
import com.redygest.grok.features.data.attribute.IAttribute;
import com.redygest.grok.features.data.attribute.ListAttribute;
import com.redygest.grok.features.data.attribute.LongAttribute;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class CoOccuranceFeatureExtractor extends AbstractFeatureExtractor {
	public CoOccuranceFeatureExtractor() {

	}

	@Override
	protected FeatureVectorCollection extract(Data t,
			IFeaturesRepository repository) {
		FeatureVector fGlobal = new FeatureVector();
		FeatureVectorCollection fCollection = new FeatureVectorCollection();

		long id = Long.valueOf(t.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVectorOld = repository.getFeatureVector(id);
		if (fVectorOld == null) {
			return null;
		}
		List<IVariable> nerVariables = new ArrayList<IVariable>();
		List<IVariable> npVariables = new ArrayList<IVariable>();

		nerVariables.addAll(fVectorOld
				.getVariablesWithAttributeType(AttributeId.NERENTITY));
		npVariables.addAll(fVectorOld
				.getVariablesWithAttributeType(AttributeId.NPENTITY));

		HashSet<String> ners = new HashSet<String>();
		for (IVariable nerVar : nerVariables) {
			String nerText = nerVar.getVariableName();
			ners.add(nerText.toLowerCase());
		}

		// merge ner and np variables. i.e remove duplicates
		for (IVariable npVar : npVariables) {
			if (!ners.contains(npVar.getVariableName().toLowerCase())) {
				nerVariables.add(npVar);
			}
		}

		// iterate over the variables
		for (int i = 0; i < nerVariables.size(); i++) {
			IVariable ivar = nerVariables.get(i);
			String ientityName = null;
			Attributes iattrs = ivar.getVariableAttributes();
			if (iattrs != null
					&& iattrs.containsAttributeType(AttributeId.SYNONYM)) {
				IAttribute synonymAttr = iattrs
						.getAttributes(AttributeId.SYNONYM);
				if (synonymAttr != null) {
					ientityName = synonymAttr.getString();
				}
			} else {
				ientityName = ivar.getVariableName();
			}

			// calculate co-occurances in this current tweet first
			List<String> coOccurances = new ArrayList<String>();
			for (int j = i + 1; j < nerVariables.size(); j++) {
				IVariable jvar = nerVariables.get(j);
				String jentityName = null;
				Attributes jattrs = jvar.getVariableAttributes();
				if (jattrs != null
						&& jattrs.containsAttributeType(AttributeId.SYNONYM)) {
					IAttribute synonymAttr = jattrs
							.getAttributes(AttributeId.SYNONYM);
					if (synonymAttr != null) {
						jentityName = synonymAttr.getString();
					}
				} else {
					jentityName = jvar.getVariableName();
				}
				coOccurances.add(jentityName);
			}

			// fetch global variable for i
			IVariable giVar = null;
			if (fGlobal.getVariable(new DataVariable(ientityName,
					FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)) != null) {
				giVar = fGlobal.getVariable(new DataVariable(ientityName,
						FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
			} else if (repository
					.getFeatureVector(FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER) != null
					&& repository
							.getFeatureVector(
									FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)
							.getVariable(
									new DataVariable(
											ientityName,
											FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)) != null) {
				giVar = repository
						.getFeatureVector(
								FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)
						.getVariable(
								new DataVariable(
										ientityName,
										FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
			}

			if (giVar == null) {
				giVar = new DataVariable(ientityName,
						FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER);
			}
			Attributes giVarAttrs = giVar.getVariableAttributes();
			List<IVariable> inpCoOccurances = null;
			if (!giVarAttrs.containsAttributeType(AttributeId.COOCCURENCE)) {
				giVarAttrs.add(new ListAttribute(AttributeId.COOCCURENCE,
						new ArrayList<IVariable>()));
			}
			IAttribute inpCoOccurAttr = giVarAttrs
					.getAttributes(AttributeId.COOCCURENCE);
			inpCoOccurances = inpCoOccurAttr.getList();

			// iterate over the co-occurances
			for (String jentityName : coOccurances) {
				// fetch global variable for j
				IVariable gjVar = null;
				if (fGlobal.getVariable(new DataVariable(jentityName,
						FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)) != null) {
					gjVar = fGlobal.getVariable(new DataVariable(jentityName,
							FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
				} else if (repository
						.getFeatureVector(FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER) != null
						&& repository
								.getFeatureVector(
										FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)
								.getVariable(
										new DataVariable(
												jentityName,
												FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)) != null) {
					gjVar = repository
							.getFeatureVector(
									FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)
							.getVariable(
									new DataVariable(
											jentityName,
											FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
				}

				if (gjVar == null) {
					gjVar = new DataVariable(jentityName,
							FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER);
				}

				Attributes gjVarAttrs = gjVar.getVariableAttributes();
				List<IVariable> jnpCoOccurances = null;
				if (!gjVarAttrs.containsAttributeType(AttributeId.COOCCURENCE)) {
					gjVarAttrs.add(new ListAttribute(AttributeId.COOCCURENCE,
							new ArrayList<IVariable>()));
				}
				IAttribute jnpCoOccurAttr = gjVarAttrs
						.getAttributes(AttributeId.COOCCURENCE);
				jnpCoOccurances = jnpCoOccurAttr.getList();

				// update global cooccurance counts of i
				long frequency = 1;
				boolean found = false;
				for (IVariable var : inpCoOccurances) {
					String varName = var.getVariableName();
					if (varName.equalsIgnoreCase(jentityName)) {
						found = true;
						IAttribute coFrequency = var.getVariableAttributes()
								.getAttributes(AttributeId.FREQUENCY);
						if (coFrequency != null) {
							frequency += coFrequency.getLong();
						}
						var.addAttribute(new LongAttribute(
								AttributeId.FREQUENCY, frequency));
					}
				}
				if (!found) {
					IVariable var = new DataVariable(jentityName, -2L);
					var.addAttribute(new LongAttribute(AttributeId.FREQUENCY,
							frequency));
					inpCoOccurances.add(var);
				}

				// update global cooccurance counts of j
				found = false;
				frequency = 1;
				for (IVariable var : jnpCoOccurances) {
					String varName = var.getVariableName();
					if (varName.equalsIgnoreCase(ientityName)) {
						found = true;
						IAttribute coFrequency = var.getVariableAttributes()
								.getAttributes(AttributeId.FREQUENCY);
						if (coFrequency != null) {
							frequency += coFrequency.getLong();
						}
						var.addAttribute(new LongAttribute(
								AttributeId.FREQUENCY, frequency));
					}
				}
				if (!found) {
					IVariable var = new DataVariable(ientityName, -2L);
					var.addAttribute(new LongAttribute(AttributeId.FREQUENCY,
							frequency));
					jnpCoOccurances.add(var);
				}
				fGlobal.addVariable(gjVar);
			}
			fGlobal.addVariable(giVar);
		}

		fCollection.put(FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER,
				fGlobal);
		return fCollection;
	}

}
