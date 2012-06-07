package com.redygest.grok.features.data.variable;

import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.IAttribute;

/**
 * Variable Interface
 * 
 */
public interface IVariable extends Comparable<IVariable> {

	/**
	 * Get variable name
	 * 
	 * @return
	 */
	public String getVariableName();

	/**
	 * Get record identifier
	 * 
	 * @return
	 */
	public Long getRecordIdentifier();

	/**
	 * Get variable attributes
	 * 
	 * @return
	 */
	public Attributes getVariableAttributes();

	/**
	 * Add attribute
	 * 
	 * @param attr
	 */
	public void addAttribute(IAttribute attr);

	/**
	 * Add attributes
	 * 
	 * @param attr
	 */
	public void addAttributes(Attributes attrs);

}
