/**
 * 
 */
package com.redygest.commons.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Mysql Store
 * 
 * @author semanticvoid
 * 
 */
public class MysqlStore {

	private Connection conn = null;
	private PreparedStatement stmt = null;

	public MysqlStore(String host, String user, String passwd, String database)
			throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + host + ":3306/" + database;
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (Exception e) {
			throw e;
		}
	}

	public ResultSet execute(String query) {
		ResultSet rs;

		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean formPreparedStmt(String query) {
		boolean status = false;

		if (conn != null && query != null) {
			try {
				this.stmt = this.conn.prepareStatement(query);
				status = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return status;
	}

	public boolean executePreparedStmt(Object... args) {
		// TODO only supports String args for now
		if (this.stmt != null) {
			int i = 1;
			for (Object arg : args) {
				if (arg instanceof String) {
					try {
						this.stmt.setString(i++, (String) arg);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

			return true;
		}

		return false;
	}
}
