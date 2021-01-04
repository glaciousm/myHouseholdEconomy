package gr.mmam.myHouseholdEconomy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gr.mmam.myHouseholdEconomy.db.SqliteConnection;
import gr.mmam.myHouseholdEconomy.model.IncomeCategory;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;

public class IncomeCategoryDAO {
	
	Connection conn;
	IncomeCategory category;
	
	public IncomeCategoryDAO() {
		conn = SqliteConnection.Connect();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			Stacktrace.print(e);
		}
	}
	
	public List<IncomeCategory> getList() {
		List<IncomeCategory> list = new ArrayList<IncomeCategory>();
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from INCOME_CATEGORY ORDER BY NAME ASC";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("name");
				category = new IncomeCategory(id,name);
				list.add(category);
				}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return list;
	}
	
	public IncomeCategory findByName(String name) {
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from INCOME_CATEGORY WHERE name = '" + name+"'";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				category = new IncomeCategory(rs.getInt("id"), rs.getString("name"));
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return category;
	}
	
	public IncomeCategory findById(int id) {
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from INCOME_CATEGORY WHERE id = '" + id+"'";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				category = new IncomeCategory(rs.getInt("id"), rs.getString("name"));
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return category;
	}
	
	public void save(IncomeCategory incomeCategory) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO INCOME_CATEGORY (name)" + "VALUES ('"+ incomeCategory.getName()+"')");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void update(IncomeCategory incomeCategory) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("UPDATE INCOME_CATEGORY SET NAME = '"+ incomeCategory.getName()+"' WHERE ID = " + incomeCategory.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void delete(IncomeCategory incomeCategory) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM INCOME_CATEGORY WHERE ID = "+incomeCategory.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

}
