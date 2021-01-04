package gr.mmam.myHouseholdEconomy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gr.mmam.myHouseholdEconomy.db.SqliteConnection;
import gr.mmam.myHouseholdEconomy.model.IncomeConcerns;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;

public class IncomeConcernsDao {
	
	Connection conn;
	IncomeConcerns concerns;
	
	public IncomeConcernsDao() {
		conn = SqliteConnection.Connect();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Stacktrace.print(e);
		}
	}
	
	public List<IncomeConcerns> getList() {
		List<IncomeConcerns> list = new ArrayList<IncomeConcerns>();
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from INCOME_CONCERNS ORDER BY NAME ASC";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("name");
				concerns = new IncomeConcerns(id,name);
				list.add(concerns);
				}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return list;
	}
	
	public IncomeConcerns findByName(String name) {
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from INCOME_CONCERNS WHERE name = '" + name+"'";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				concerns = new IncomeConcerns(rs.getInt("id"), rs.getString("name"));
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return concerns;
	}
	
	public IncomeConcerns findById(int id) {
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from INCOME_CONCERNS WHERE id = '" + id+"'";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				concerns = new IncomeConcerns(rs.getInt("id"), rs.getString("name"));
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return concerns;
	}
	
	public void save(IncomeConcerns incomeConcerns) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO INCOME_CONCERNS (name)" + "VALUES ('"+ incomeConcerns.getName()+"')");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void update(IncomeConcerns incomeConcerns) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("UPDATE INCOME_CONCERNS SET NAME = '" + incomeConcerns.getName() + "' WHERE ID = " + incomeConcerns.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void delete(IncomeConcerns incomeConcerns) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM INCOME_CONCERNS WHERE ID = "+incomeConcerns.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

}
