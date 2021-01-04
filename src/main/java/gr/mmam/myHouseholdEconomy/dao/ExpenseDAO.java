package gr.mmam.myHouseholdEconomy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gr.mmam.myHouseholdEconomy.db.SqliteConnection;
import gr.mmam.myHouseholdEconomy.model.Expense;
import gr.mmam.myHouseholdEconomy.model.ExpenseCategory;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;

@SuppressWarnings("unused")
public class ExpenseDAO {
	
	Connection conn;
	
	public ExpenseDAO() {
		conn = SqliteConnection.Connect();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Stacktrace.print(e);
		}
	}
	
	public List<Expense> getList() {
		Expense expense;
		List<Expense> list = new ArrayList<Expense>();
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from EXPENSES" + " ORDER BY NAME ASC";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("name");
				int categoryId = rs.getInt("CATEGORY_ID");
				expense = new Expense(id,name,categoryId);
				list.add(expense);
				}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return list;
	}
	
	public Expense findByName(String name) {
		Expense expense = null;
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from EXPENSES WHERE name = '" + name+"'";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString("name").equals(name)) {
					expense = new Expense(rs.getInt("id"), rs.getString("name"),rs.getInt("CATEGORY_ID"));
				}
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return expense;
	}
	
	public Expense findById(int id) {
		Expense expense = null;
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from EXPENSES WHERE id = " + id;
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				expense = new Expense(rs.getInt("id"), rs.getString("name"),rs.getInt("CATEGORY_ID"));
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return expense;
	}
	
	public List<Expense> findByCategoryId(int id) {
		List<Expense> list = new ArrayList<>();
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from EXPENSES WHERE CATEGORY_ID = " + id + " ORDER BY NAME ASC";
		try {
			preparedStatement= conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				list.add(new Expense(rs.getInt("id"), rs.getString("name"),rs.getInt("CATEGORY_ID")));
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return list;
	}
	
	public void save(Expense expense) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO EXPENSES (name, category_id)" + "VALUES ('"+ expense.getName()+"','"+expense.getCategoryId()+"')");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void update(Expense expense) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("UPDATE EXPENSES SET NAME = '"+ expense.getName()+"', CATEGORY_ID = "+expense.getCategoryId()+" WHERE ID = " + expense.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void delete(Expense expense) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM EXPENSES WHERE ID = "+expense.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

}
