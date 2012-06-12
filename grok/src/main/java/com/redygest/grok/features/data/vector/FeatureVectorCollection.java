package com.redygest.grok.features.data.vector;

import java.util.Collection;
import java.util.HashMap;

import com.redygest.grok.features.data.variable.IVariable;

/**
 * Feature Vector Collection Class
 * 
 */
public class FeatureVectorCollection extends HashMap<Long, FeatureVector> {

	// global record identifier
	public static final long GLOBAL_RECORD_IDENTIFIER = -1;

	/**
	 * Constructor
	 */
	public FeatureVectorCollection() {
	}

	/**
	 * Get all feature vectors
	 * 
	 * @return
	 */
	public Collection<FeatureVector> getFeatureVectors() {
		return this.values();
	}

	/**
	 * Get all record ids
	 * 
	 * @return
	 */
	public Collection<Long> getRecordIdentifiers() {
		return this.keySet();
	}

	/**
	 * Get feature vector for record id
	 * 
	 * @param recordIdentifier
	 * @return
	 */
	public FeatureVector getFeatureVector(long recordIdentifier) {
		return this.get(recordIdentifier);
	}

	/**
	 * Add feature vectors
	 * 
	 * @param featureVectors
	 */
	public void addFeatures(FeatureVectorCollection featureCollection) {
		if (featureCollection != null) {
			for (long rId : featureCollection.getRecordIdentifiers()) {
				if (this.containsKey(rId)) {
					FeatureVector featureVector = this.get(rId);
					for (IVariable variable : featureCollection
							.getFeatureVector(rId).getVariables()) {
						featureVector.addVariable(variable);
					}
				} else {
					this.put(rId, featureCollection.getFeatureVector(rId));
				}
			}
		}
	}

	// /**
	// * Add global features
	// *
	// * @param fv
	// * @param overwrite
	// */
	// public void addGlobalFeatures(FeatureVector fv, boolean overwrite) {
	// if (this.featureVectors.containsKey(GLOBAL_RECORD_IDENTIFIER)) {
	// FeatureVector featureVector = this.featureVectors
	// .get(GLOBAL_RECORD_IDENTIFIER);
	// for (IVariable variable : fv.getVariables()) {
	// if (overwrite == true
	// && featureVector.getVariable(variable) != null) {
	// featureVector.addVariable(variable);
	// } else {
	// IVariable existingVariable = featureVector
	// .getVariable(variable);
	// if (existingVariable != null) {
	// featureVector.addVariable(merge(variable,
	// existingVariable));
	// } else {
	// featureVector.addVariable(variable);
	// }
	// }
	// }
	// } else {
	// this.featureVectors.put(GLOBAL_RECORD_IDENTIFIER, fv);
	// }
	// }

	// private IVariable merge(IVariable newVariable, IVariable
	// existingVariable) {
	// Attributes newAttrs = newVariable.getVariableAttributes();
	// Attributes existingAttrs = existingVariable.getVariableAttributes();
	// Attributes rAttrs = new Attributes();
	// rAttrs.putAll(existingAttrs);
	// for (AttributeId type : newAttrs.getAttributesMap().keySet()) {
	// List<String> newAttributeNames = newAttrs.getAttributeNames(type);
	// if (newAttributeNames != null && newAttributeNames.size() > 1) {
	// throw new RuntimeException(
	// "cannot update a multi valued attribute type in newAttributeNames");
	// }
	// List<String> existingAttributeNames = existingAttrs
	// .getAttributeNames(type);
	// if (existingAttributeNames != null
	// && existingAttributeNames.size() > 1) {
	// throw new RuntimeException(
	// "cannot update a multi valued attribute type in existingAttributeNames");
	// }
	//
	// if (newAttributeNames != null) {
	// String newAttributeName = newAttributeNames.get(0);
	// if (existingAttributeNames != null) {
	// String existingAttributeName = existingAttributeNames
	// .get(0);
	// Double rAttributeName = Double.valueOf(newAttributeName)
	// + Double.valueOf(existingAttributeName);
	// rAttrs.put(type, Double.toString(rAttributeName));
	// } else {
	// rAttrs.put(type, newAttributeName);
	// }
	// }
	// }
	// DataVariable rVariable = new DataVariable(
	// existingVariable.getVariableName(),
	// existingVariable.getRecordIdentifier());
	// rVariable.addAttributes(rAttrs);
	// return rVariable;
	// }
}
