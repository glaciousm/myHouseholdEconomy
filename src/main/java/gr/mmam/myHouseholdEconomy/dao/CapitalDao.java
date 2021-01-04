package gr.mmam.myHouseholdEconomy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gr.mmam.myHouseholdEconomy.db.SqliteConnection;
import gr.mmam.myHouseholdEconomy.model.Capital;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;

public class CapitalDao {

	Connection conn;
	Capital capital;

	public CapitalDao() {
		conn = SqliteConnection.Connect();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Stacktrace.print(e);
		}
	}

	public List<Capital> getList() {
		List<Capital> list = new ArrayList<Capital>();
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from CAPITAL ORDER BY DATE ASC";
		try {
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				double value = rs.getDouble("VALUE");
				String comment = rs.getString("COMMENT");
				String date = rs.getString("DATE");
				double checkpoint = rs.getDouble("CHECKPOINT");
				capital = new Capital(id, value, comment, date, checkpoint);
				list.add(capital);
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return list;
	}
	
	public Capital getLastCheckValue() {
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from CAPITAL ORDER BY ID DESC LIMIT 1";
		try {
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				double value = rs.getDouble("VALUE");
				String comment = rs.getString("COMMENT");
				String date = rs.getString("DATE");
				double checkpoint = rs.getDouble("CHECKPOINT");
				capital = new Capital(id, value, comment, date, checkpoint);
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return capital;
	}

	public void save(Capital capital) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate(
					"INSERT INTO CAPITAL (VALUE, COMMENT, DATE, CHECKPOINT)"
							+ " VALUES ("+ capital.getValue() + ",'" + capital.getComment() + "','" + capital.getDate() + "',"+capital.getCheckpoint()+ ")");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
}
