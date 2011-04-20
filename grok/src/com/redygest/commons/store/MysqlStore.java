/**
 * 
 */
package com.redygest.commons.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * Mysql Store
 * @author semanticvoid
 *
 */
public class MysqlStore {
	
	private Connection conn = null;

	public MysqlStore(String host, String user, String passwd, String database) throws Exception {
		try {
		      Class.forName("com.mysql.jdbc.Driver");
		      String url = "jdbc:mysql://" + host + ":3306/" + database;
		      conn =	DriverManager.getConnection(url, user, passwd);
		    }catch( Exception e ) {
		      throw e;
		    }
	}
	
	public ResultSet execute(String query) {
		ResultSet rs;
		
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return rs;
	}
	
	public boolean executeUpdate(String query) {
		try {
			Statement stmt = conn.createStatement();
			query.replaceAll("\"", "\\\"");
			return stmt.execute(query);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
