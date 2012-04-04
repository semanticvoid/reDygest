package com.redygest.grok.selection;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Query;

/**
 * Selector Interface
 * 
 */
public interface ISelector {

	/**
	 * Select logic
	 * 
	 * @param size
	 * @param data
	 * @param query
	 * @return
	 */
	List<Data> select(int size, List<Data> data, Query query);

	/**
	 * Select logic
	 * 
	 * @param size
	 * @param data
	 * @return
	 */
	List<Data> select(int size, List<Data> data);

}
