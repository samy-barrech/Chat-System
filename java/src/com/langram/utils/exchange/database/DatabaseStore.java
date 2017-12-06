package com.langram.utils.exchange.database;

import com.langram.utils.Settings;

import java.sql.*;

/**
 * This file is part of the project java.
 *
 * @author Guillaume
 * @version 1.0
 * @date 04/12/2017
 * @since 1.0
 */
public class DatabaseStore {
	public static final String JDBC_SQLITE = "jdbc:sqlite:";
	private static DatabaseStore databaseStore;
	private final String db_path;

	private DatabaseStore() {
		this.db_path = Settings.getInstance().getDatabasePath();
	}

	public static synchronized DatabaseStore getInstance() {
		if (databaseStore == null) {
			databaseStore = new DatabaseStore();
		}
		databaseStore.createSchema();
		return databaseStore;
	}

	private String getConnectionUrl() {
		return JDBC_SQLITE + this.db_path;
	}

	public Connection connect() {

		Connection conn = null;
		try {

			String url = this.getConnectionUrl();
			conn = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return conn;
	}

	private void createSchema() {

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS channel (\n"
				+ "	id VARCHAR PRIMARY KEY,\n"
				+ "	channelName TEXT NOT NULL,\n"
				+ "	ipAddress VARCHAR(15),\n"
				+ " active INTEGER\n"
				+ ");";

		try (Connection conn = this.connect()) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");

				Statement stmt = conn.createStatement();
				stmt.execute(sql);

				conn.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}