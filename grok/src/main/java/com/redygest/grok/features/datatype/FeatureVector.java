package com.redygest.grok.features.datatype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FeatureVector implements Serializable {

	private static final long serialVersionUID = -8660535858575993066L;
	Map<Variable, Variable> variables = new HashMap<Variable, Variable>();
	
	public void addVariable(Variable variable) {
		if(variables.containsKey(variable)) {
			Variable thisVariable = variables.get(variable);
			thisVariable.addAttributes(variable.getVariableAttributes());
		} else {
			variables.put(variable, variable);
		}
	}
	
	public Variable getVariable(Variable queryVariable) {
		return variables.get(queryVariable);
	}
	
	public List<Variable> getVariablesWithAttributeType(AttributeType type) {
		List<Variable> variables = new ArrayList<Variable>();
		for(Variable var : this.variables.keySet()) {
			Attributes attrs = var.getVariableAttributes();
			if(attrs.containsAttributeType(type)) {
				variables.add(var);
			}
		}
		
		return variables;
	}
	
	public Collection<Variable> getVariables() {
		return variables.values();
	}
}
