package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.BooleanAttribute;
import com.redygest.grok.features.data.attribute.IAttribute;
import com.redygest.grok.features.data.attribute.LongAttribute;
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
			features.put(FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER,
					extract(t, repository));
		}
		return features;
	}

	@Override
	protected FeatureVector extract(Data t, IFeaturesRepository repository) {
		FeatureVector fVector = new FeatureVector();

		long id = Long.valueOf(t.getValue(DataType.RECORD_IDENTIFIER));
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
				IAttribute synonymAttr = attrs
						.getAttributes(AttributeId.SYNONYM);
				if (synonymAttr != null) {
					entityName = synonymAttr.getString();
				}
			} else {
				entityName = var.getVariableName();
			}

			if (attrs.containsAttributeType(AttributeId.NERENTITY)) {
				entityType = AttributeId.NERENTITY;
			} else if (attrs.containsAttributeType(AttributeId.NPENTITY)) {
				entityType = AttributeId.NPENTITY;
			}

			long frequency = 1;
			IVariable gVar = fVector.getVariable(new DataVariable(entityName,
					FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER));
			if (gVar == null) {
				gVar = new DataVariable(entityName,
						FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER);
			}
			Attributes gVarAttrs = gVar.getVariableAttributes();
			gVarAttrs.add(new BooleanAttribute(AttributeId.ENTITY, true));
			gVarAttrs.add(new BooleanAttribute(entityType, true));

			if (gVarAttrs.containsAttributeType(AttributeId.FREQUENCY)) {
				IAttribute freqAttr = gVarAttrs
						.getAttributes(AttributeId.FREQUENCY);
				if (freqAttr != null) {
					frequency += freqAttr.getLong();
				}
			}

			gVarAttrs.add(new LongAttribute(AttributeId.FREQUENCY, frequency));

			gVar.addAttributes(gVarAttrs);
			fVector.addVariable(gVar);
		}

		return fVector;
	}
}
