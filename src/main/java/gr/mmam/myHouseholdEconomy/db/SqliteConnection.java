package gr.mmam.myHouseholdEconomy.db;

import java.sql.Connection;
import java.sql.DriverManager;

import gr.mmam.myHouseholdEconomy.util.Stacktrace;

public class SqliteConnection {
	static ClassLoader classLoader = SqliteConnection.class.getClassLoader();
	
	public static Connection Connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:database.db");
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return conn;
	}

}
