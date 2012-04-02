package com.redygest.grok.selection;

import java.util.List;

import com.redygest.commons.data.Data;

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
	 * @param queryMembers
	 * @return
	 */
	List<Data> select(int size, List<Data> data, List<String> queryMembers);

	/**
	 * Select logic
	 * 
	 * @param size
	 * @param data
	 * @return
	 */
	List<Data> select(int size, List<Data> data);

}
