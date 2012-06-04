package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class EntityFeatureExtractor extends AbstractFeatureExtractor {

	public EntityFeatureExtractor() {
	}

	@Override
	public FeatureVectorCollection extract(List<Data> dataList,
			IFeaturesRepository repository) {
		FeatureVectorCollection features = new FeatureVectorCollection();
		for (Data t : dataList) {
			features.addGlobalFeatures(extract(t, repository), true);
		}
		return features;
	}

	@Override
	protected FeatureVector extract(Data t, IFeaturesRepository repository) {
		FeatureVector fVector = new FeatureVector();

		String id = t.getValue(DataType.RECORD_IDENTIFIER);
		FeatureVector fVectorOld = repository.getFeatureVector(id);
		if (fVectorOld == null) {
			return fVector;
		}

		List<Variable> variables = new ArrayList<Variable>();
		variables.addAll(fVectorOld
				.getVariablesWithAttributeType(AttributeType.NERENTITY));
		variables.addAll(fVectorOld
				.getVariablesWithAttributeType(AttributeType.NPENTITY));

		for (Variable var : variables) {
			String entityName = null;
			AttributeType entityType = null;
			Attributes attrs = var.getVariableAttributes();

			if (attrs != null
					&& attrs.containsAttributeType(AttributeType.SYNONYM)) {
				List<String> attrNames = attrs
						.getAttributeNames(AttributeType.SYNONYM);
				if (attrNames != null) {
					entityName = attrNames.get(0);
				}
			} else {
				entityName = var.getVariableName();
			}

			if (attrs.containsAttributeType(AttributeType.NERENTITY)) {
				entityType = AttributeType.NERENTITY;
			} else if (attrs.containsAttributeType(AttributeType.NPENTITY)) {
				entityType = AttributeType.NPENTITY;
			}

			int frequency = 1;
			Variable gVar = fVector.getVariable(new DataVariable(entityName,
					FeatureVectorCollection.GLOBAL_IDENTIFIER));
			if (gVar == null) {
				gVar = new DataVariable(entityName,
						FeatureVectorCollection.GLOBAL_IDENTIFIER);
			}
			Attributes gVarAttrs = gVar.getVariableAttributes();
			gVarAttrs.put(AttributeType.ENTITY, "true");
			gVarAttrs.put(entityType, "true");

			if (gVarAttrs.containsAttributeType(AttributeType.FREQUENCY)) {
				List<String> values = gVarAttrs
						.getAttributeNames(AttributeType.FREQUENCY);
				if (values != null) {
					frequency += Integer.valueOf(values.get(0));
				}
			}

			gVarAttrs.put(AttributeType.FREQUENCY, String.valueOf(frequency));

			gVar.addAttributes(gVarAttrs);
			fVector.addVariable(gVar);
		}

		return fVector;
	}
}
