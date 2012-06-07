package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
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

		List<IVariable> variables = new ArrayList<IVariable>();
		variables.addAll(fVectorOld
				.getVariablesWithAttributeType(AttributeId.NERENTITY));
		variables.addAll(fVectorOld
				.getVariablesWithAttributeType(AttributeId.NPENTITY));

		for (IVariable var : variables) {
			String entityName = null;
			AttributeId entityType = null;
			Attributes attrs = var.getVariableAttributes();

			if (attrs != null
					&& attrs.containsAttributeType(AttributeId.SYNONYM)) {
				List<String> attrNames = attrs
						.getAttributeNames(AttributeId.SYNONYM);
				if (attrNames != null) {
					entityName = attrNames.get(0);
				}
			} else {
				entityName = var.getVariableName();
			}

			if (attrs.containsAttributeType(AttributeId.NERENTITY)) {
				entityType = AttributeId.NERENTITY;
			} else if (attrs.containsAttributeType(AttributeId.NPENTITY)) {
				entityType = AttributeId.NPENTITY;
			}

			int frequency = 1;
			IVariable gVar = fVector.getVariable(new DataVariable(entityName,
					FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
			if (gVar == null) {
				gVar = new DataVariable(entityName,
						FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER);
			}
			Attributes gVarAttrs = gVar.getVariableAttributes();
			gVarAttrs.put(AttributeId.ENTITY, "true");
			gVarAttrs.put(entityType, "true");

			if (gVarAttrs.containsAttributeType(AttributeId.FREQUENCY)) {
				List<String> values = gVarAttrs
						.getAttributeNames(AttributeId.FREQUENCY);
				if (values != null) {
					frequency += Integer.valueOf(values.get(0));
				}
			}

			gVarAttrs.put(AttributeId.FREQUENCY, String.valueOf(frequency));

			gVar.addAttributes(gVarAttrs);
			fVector.addVariable(gVar);
		}

		return fVector;
	}
}
