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

/**
 * Entity Cooccurrence Feature Extractor
 * 
 */
public class EntityCoccurrenceFeatureExtractor extends AbstractFeatureExtractor {

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

		List<IVariable> remainingNpVariables = new ArrayList<IVariable>();
		for (IVariable npVar : npVariables) {
			if (!ners.contains(npVar.getVariableName().toLowerCase())) {
				remainingNpVariables.add(npVar);
			}
		}

		// iterate over the variables
		for (int i = 0; i < nerVariables.size(); i++) {
			IVariable iVar = nerVariables.get(i);
			String iEntityName = null;
			Attributes iAttrs = iVar.getVariableAttributes();

			if (iAttrs != null
					&& iAttrs.containsAttributeType(AttributeId.SYNONYM)) {
				IAttribute synonymAttr = iAttrs
						.getAttributes(AttributeId.SYNONYM);
				if (synonymAttr != null) {
					iEntityName = synonymAttr.getString();
				}
			} else {
				iEntityName = iVar.getVariableName();
			}

			// calculate co-occurrences in this current tweet first
			List<String> coOccurrences = new ArrayList<String>();
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

				coOccurrences.add(jentityName);
			}

			// fetch global variable for i
			IVariable iGlobalVar = null;
			if (fGlobal.getVariable(new DataVariable(iEntityName,
					FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)) != null) {
				iGlobalVar = fGlobal.getVariable(new DataVariable(iEntityName,
						FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
			} else if (repository
					.getFeatureVector(FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER) != null
					&& repository
							.getFeatureVector(
									FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)
							.getVariable(
									new DataVariable(
											iEntityName,
											FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)) != null) {
				iGlobalVar = repository
						.getFeatureVector(
								FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)
						.getVariable(
								new DataVariable(
										iEntityName,
										FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
			}

			if (iGlobalVar == null) {
				iGlobalVar = new DataVariable(iEntityName,
						FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER);
			}

			Attributes iGlobalVarAttrs = iGlobalVar.getVariableAttributes();
			List<IVariable> inpCoOccurances = null;
			if (!iGlobalVarAttrs
					.containsAttributeType(AttributeId.ENTITYCOOCCURENCE)) {
				iGlobalVarAttrs.add(new ListAttribute(
						AttributeId.ENTITYCOOCCURENCE,
						new ArrayList<IVariable>()));
			}

			IAttribute inpCoOccurAttr = iGlobalVarAttrs
					.getAttributes(AttributeId.ENTITYCOOCCURENCE);
			inpCoOccurances = inpCoOccurAttr.getList();

			// iterate over the co-occurrences
			for (String jEntityName : coOccurrences) {
				// fetch global variable for j
				IVariable jGlobalVar = null;
				if (fGlobal.getVariable(new DataVariable(jEntityName,
						FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)) != null) {
					jGlobalVar = fGlobal.getVariable(new DataVariable(
							jEntityName,
							FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
				} else if (repository
						.getFeatureVector(FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER) != null
						&& repository
								.getFeatureVector(
										FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)
								.getVariable(
										new DataVariable(
												jEntityName,
												FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)) != null) {
					jGlobalVar = repository
							.getFeatureVector(
									FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER)
							.getVariable(
									new DataVariable(
											jEntityName,
											FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
				}

				if (jGlobalVar == null) {
					jGlobalVar = new DataVariable(jEntityName,
							FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER);
				}

				Attributes jGlobalVarAttrs = jGlobalVar.getVariableAttributes();
				List<IVariable> jnpCoOccurances = null;

				if (!jGlobalVarAttrs
						.containsAttributeType(AttributeId.ENTITYCOOCCURENCE)) {
					jGlobalVarAttrs.add(new ListAttribute(
							AttributeId.ENTITYCOOCCURENCE,
							new ArrayList<IVariable>()));
				}

				IAttribute jnpCoOccurAttr = jGlobalVarAttrs
						.getAttributes(AttributeId.ENTITYCOOCCURENCE);
				jnpCoOccurances = jnpCoOccurAttr.getList();

				// update global cooccurrence counts of i
				long frequency = 1;
				boolean found = false;
				for (IVariable var : inpCoOccurances) {
					String varName = var.getVariableName();

					if (varName.equalsIgnoreCase(jEntityName)) {
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
					IVariable var = new DataVariable(jEntityName, -2L);
					var.addAttribute(new LongAttribute(AttributeId.FREQUENCY,
							frequency));
					AttributeId entityType = null;
					if (jGlobalVarAttrs != null
							&& jGlobalVarAttrs
									.containsAttributeType(AttributeId.NPENTITY)) {
						entityType = AttributeId.NPENTITY;
					} else {
						entityType = AttributeId.NERENTITY;
					}
					var.addAttribute(new BooleanAttribute(AttributeId.ENTITY,
							true));
					var.addAttribute(new BooleanAttribute(entityType, true));
					inpCoOccurances.add(var);
				}

				// update global cooccurance counts of j
				found = false;
				frequency = 1;
				for (IVariable var : jnpCoOccurances) {
					String varName = var.getVariableName();

					if (varName.equalsIgnoreCase(iEntityName)) {
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
					IVariable var = new DataVariable(iEntityName, -2L);
					var.addAttribute(new LongAttribute(AttributeId.FREQUENCY,
							frequency));
					AttributeId entityType = null;
					if (iGlobalVarAttrs != null
							&& iGlobalVarAttrs
									.containsAttributeType(AttributeId.NPENTITY)) {
						entityType = AttributeId.NPENTITY;
					} else {
						entityType = AttributeId.NERENTITY;
					}
					var.addAttribute(new BooleanAttribute(AttributeId.ENTITY,
							true));
					var.addAttribute(new BooleanAttribute(entityType, true));

					jnpCoOccurances.add(var);
				}

				fGlobal.addVariable(jGlobalVar);
			}

			fGlobal.addVariable(iGlobalVar);
		}

		fCollection.put(FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER,
				fGlobal);
		return fCollection;
	}

}
