package com.redygest.grok.store;

import com.redygest.commons.store.MysqlStore;

/**
 * 
 * Dygest MySQL Store
 * 
 * @author semanticvoid
 * 
 */
public class DygestMysqlStore implements IStore {

	private MysqlStore db = null;

	public DygestMysqlStore() {
		try {
			// TODO read creds from conf
			db = new MysqlStore("localhost", "root", "", "redygest");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to store story json
	 */
	public boolean write(String storyJSON) {
		String query = "INSERT INTO stories (story_json) values (?)";

		if (this.db != null && storyJSON != null) {
			if (this.db.formPreparedStmt(query)) {
				return this.db.executePreparedStmt(storyJSON);
			}
		}

		return false;
	}
}
