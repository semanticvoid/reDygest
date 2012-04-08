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
	 * @return
	 */
	List<Data> select(int size);

}
