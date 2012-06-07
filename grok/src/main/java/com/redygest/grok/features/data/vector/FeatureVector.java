package com.redygest.grok.features.data.vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.IVariable;

/**
 * Feature Vector Class
 * 
 * @author semanticvoid
 * 
 */
public class FeatureVector implements Serializable {

	// variable map
	Map<IVariable, IVariable> variables = new LinkedHashMap<IVariable, IVariable>();

	/**
	 * Add variable to feature vector
	 * 
	 * @param variable
	 */
	public void addVariable(IVariable variable) {
		if (variables.containsKey(variable)) {
			IVariable thisVariable = variables.get(variable);
			thisVariable.addAttributes(variable.getVariableAttributes());
		} else {
			variables.put(variable, variable);
		}
	}

	/**
	 * Get variable
	 * 
	 * @param queryVariable
	 * @return
	 */
	public IVariable getVariable(IVariable queryVariable) {
		return variables.get(queryVariable);
	}

	/**
	 * Get variables with attribute id
	 * 
	 * @param type
	 * @return
	 */
	public List<IVariable> getVariablesWithAttributeType(AttributeId id) {
		List<IVariable> variables = new ArrayList<IVariable>();

		for (IVariable var : this.variables.keySet()) {
			Attributes attrs = var.getVariableAttributes();
			if (attrs.containsAttributeType(id)) {
				variables.add(var);
			}
		}

		return variables;
	}

	/**
	 * Get all variables
	 * 
	 * @return
	 */
	public Collection<IVariable> getVariables() {
		return variables.values();
	}
}
