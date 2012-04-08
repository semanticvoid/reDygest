/**
 * 
 */
package com.redygest.grok.ranking;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Query;

/**
 * Ranking Interface
 * 
 */
public interface IRanking {

	/**
	 * Rank against query
	 * 
	 * @param query
	 * @return ranked List
	 */
	public List<Data> rank(Query query);

}
