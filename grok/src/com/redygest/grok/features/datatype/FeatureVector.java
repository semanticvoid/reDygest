package com.redygest.grok.features.datatype;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
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
	
	public Collection<Variable> getVariables() {
		return variables.values();
	}
}
