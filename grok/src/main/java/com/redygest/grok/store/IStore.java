/**
 * 
 */
package com.redygest.grok.store;

/**
 * Dygest Store Interface
 * 
 * @author semanticvoid
 * 
 */
public interface IStore {

	/**
	 * Function to store Story JSON
	 * 
	 * @param storyJSON
	 * @return true on success, false otherwise
	 */
	public boolean write(String storyJSON);

}
