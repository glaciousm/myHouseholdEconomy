package gr.mmam.myHouseholdEconomy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gr.mmam.myHouseholdEconomy.db.SqliteConnection;
import gr.mmam.myHouseholdEconomy.model.ExpenseCategory;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;

public class ExpenseCategoryDAO {
	
	Connection conn;
	
	public ExpenseCategoryDAO() {
		conn = SqliteConnection.Connect();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Stacktrace.print(e);
		}
	}
	
	public List<ExpenseCategory> getList() {
		ExpenseCategory category;
		List<ExpenseCategory> list = new ArrayList<ExpenseCategory>();
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from EXPENSE_CATEGORY" + " ORDER BY NAME ASC";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("name");
				category = new ExpenseCategory(id,name);
				list.add(category);
				}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return list;
	}
	
	public ExpenseCategory findByName(String name) {
		ExpenseCategory category = null;
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from EXPENSE_CATEGORY WHERE name = '" + name+"'";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString("name").equals(name)) {
					category = new ExpenseCategory(rs.getInt("id"), rs.getString("name"));
				}
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return category;
	}
	
	public ExpenseCategory findById(int id) {
		ExpenseCategory category = null;
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from EXPENSE_CATEGORY WHERE id = '" + id+"'";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				category = new ExpenseCategory(rs.getInt("id"), rs.getString("name"));
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return category;
	}
	
	public void save(ExpenseCategory expenseCategory) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO EXPENSE_CATEGORY (name)" + "VALUES ('"+ expenseCategory.getName()+"')");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void update(ExpenseCategory expenseCategory) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("UPDATE EXPENSE_CATEGORY SET NAME = '"+ expenseCategory.getName()+"' WHERE ID = " + expenseCategory.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void delete(ExpenseCategory expenseCategory) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM EXPENSE_CATEGORY WHERE ID = "+expenseCategory.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

}
