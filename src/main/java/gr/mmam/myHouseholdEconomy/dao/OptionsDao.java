package gr.mmam.myHouseholdEconomy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gr.mmam.myHouseholdEconomy.db.SqliteConnection;
import gr.mmam.myHouseholdEconomy.model.Capital;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;

public class OptionsDao {

	Connection conn;
	Capital capital;

	public OptionsDao() {
		conn = SqliteConnection.Connect();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			Stacktrace.print(e);
		}
	}

	public void clearOutcomes() {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM OUTCOME");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	public void clearExpenses() {
		clearOutcomes();
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM EXPENSES");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	public void clearExpenseCategories() {
		clearOutcomes();
		clearExpenses();
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM EXPENSE_CATEGORY");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public String getCurrency() {
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select VALUE_STR from VARIOUS WHERE TYPE = 'Currency'";
		String currency = "";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				currency = rs.getString("VALUE_STR");
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return currency;
	}
	
	public void setCurrency(String currency) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate(
					"UPDATE VARIOUS SET VALUE_STR = '" + currency + "' WHERE TYPE = 'Currency'");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void clearDb() {
		clearOutcomes();
		clearExpenses();
		clearExpenseCategories();
	}
}
