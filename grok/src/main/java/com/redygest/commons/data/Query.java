/**
 * 
 */
package com.redygest.commons.data;

import java.util.List;

/**
 * Query Data Class
 */
public class Query extends AbstractData {

	/**
	 * Constructor
	 * 
	 * @param queryTerms
	 */
	public Query(List<String> queryTerms) {
		setValues(DataType.BODY_TOKENIZED, queryTerms);
	}

	@Override
	protected boolean isDataPopulated() {
		return true;
	}

}
