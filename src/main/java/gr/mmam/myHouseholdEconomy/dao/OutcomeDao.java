package gr.mmam.myHouseholdEconomy.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import gr.mmam.myHouseholdEconomy.db.SqliteConnection;
import gr.mmam.myHouseholdEconomy.model.Expense;
import gr.mmam.myHouseholdEconomy.model.Outcome;
import gr.mmam.myHouseholdEconomy.model.SumModel;
import gr.mmam.myHouseholdEconomy.util.DateUtil;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;

public class OutcomeDao {

	Connection conn;
	Outcome outcome;

	public OutcomeDao() {
		conn = SqliteConnection.Connect();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			Stacktrace.print(e);
		}
	}

	public List<Outcome> getList() {
		List<Outcome> list = new ArrayList<Outcome>();
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from OUTCOME";
		try {
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				int categoryId = rs.getInt("EXPENSE_ID");
				int expenseId = rs.getInt("CATEGORY_ID");
				double valuer = rs.getDouble("VALUE");
				Date date = rs.getDate("DATE");
				String comment = rs.getString("COMMENT");
				outcome = new Outcome(id, categoryId, expenseId, valuer, date, comment);
				list.add(outcome);
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return list;
	}

	public Outcome findById(int id) {
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from OUTCOME WHERE id = " + id;
		try {
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int categoryId = rs.getInt("EXPENSE_ID");
				int expenseId = rs.getInt("CATEGORY_ID");
				double valuer = rs.getDouble("VALUE");
				Date date = rs.getDate("DATE");
				String comment = rs.getString("COMMENT");
				outcome = new Outcome(id, categoryId, expenseId, valuer, date, comment);
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return outcome;
	}

	public List<Outcome> findByMonthAndYear(int month, int year) {
		List<Outcome> list = new ArrayList<Outcome>();
		if (year != 0) {
			PreparedStatement preparedStatement;
			ResultSet rs = null;
			String query = "SELECT * FROM OUTCOME WHERE strftime('%m', DATE) = '" + DateUtil.zeroPadMonth(month)
					+ "' AND STRFTIME('%Y', DATE) = '" + year + "' ORDER BY DATE DESC";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("ID");
					int categoryId = rs.getInt("EXPENSE_ID");
					int expenseId = rs.getInt("CATEGORY_ID");
					double valuer = rs.getDouble("VALUE");
					String date = rs.getString("DATE");
					String comment = rs.getString("COMMENT");
					outcome = new Outcome(id, categoryId, expenseId, valuer, Date.valueOf(date), comment);
					list.add(outcome);
				}
				conn.commit();
				rs.close();
			} catch (Exception e) {
				Stacktrace.print(e);
			}
		}
		return list;
	}
	
	public List<Outcome> findByMonthAndYearByValueTopFive(int month, int year) {
		List<Outcome> list = new ArrayList<Outcome>();
		if (year != 0) {
			PreparedStatement preparedStatement;
			ResultSet rs = null;
			String query = "SELECT ID,EXPENSE_ID, CATEGORY_ID, SUM(VALUE), DATE, COMMENT FROM OUTCOME WHERE strftime('%m', DATE) = '" + DateUtil.zeroPadMonth(month) + "' AND STRFTIME('%Y', DATE) = '" + year + "' GROUP BY CATEGORY_ID ORDER BY SUM(VALUE) DESC LIMIT 5";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("ID");
					int categoryId = rs.getInt("EXPENSE_ID");
					int expenseId = rs.getInt("CATEGORY_ID");
					double value = rs.getDouble("SUM(VALUE)");
					String date = rs.getString("DATE");
					String comment = rs.getString("COMMENT");
					double roundOff = Math.round(value * 100.0) / 100.0;
					outcome = new Outcome(id, categoryId, expenseId, roundOff, Date.valueOf(date), comment);
					list.add(outcome);
				}
				conn.commit();
				rs.close();
			} catch (Exception e) {
				Stacktrace.print(e);
			}
		}
		return list;
	}
	
	public List<Outcome> findByYearByValueTopFive(int year) {
		List<Outcome> list = new ArrayList<Outcome>();
		if (year != 0) {
			PreparedStatement preparedStatement;
			ResultSet rs = null;
			String query = "SELECT ID,EXPENSE_ID, CATEGORY_ID, SUM(VALUE), DATE, COMMENT FROM OUTCOME WHERE STRFTIME('%Y', DATE) = '" + year + "' GROUP BY CATEGORY_ID ORDER BY SUM(VALUE) DESC LIMIT 5";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("ID");
					int categoryId = rs.getInt("EXPENSE_ID");
					int expenseId = rs.getInt("CATEGORY_ID");
					double value = rs.getDouble("SUM(VALUE)");
					String date = rs.getString("DATE");
					String comment = rs.getString("COMMENT");
					double roundOff = Math.round(value * 100.0) / 100.0;
					outcome = new Outcome(id, categoryId, expenseId, roundOff, Date.valueOf(date), comment);
					list.add(outcome);
				}
				conn.commit();
				rs.close();
			} catch (Exception e) {
				Stacktrace.print(e);
			}
		}
		return list;
	}
	
	public List<Outcome> findByYearByValue(int year) {
		List<Outcome> list = new ArrayList<Outcome>();
		if (year != 0) {
			PreparedStatement preparedStatement;
			ResultSet rs = null;
			String query = "SELECT ID,EXPENSE_ID, CATEGORY_ID, SUM(VALUE), DATE, COMMENT FROM OUTCOME WHERE STRFTIME('%Y', DATE) = '" + year + "' GROUP BY CATEGORY_ID ORDER BY SUM(VALUE) DESC";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("ID");
					int categoryId = rs.getInt("EXPENSE_ID");
					int expenseId = rs.getInt("CATEGORY_ID");
					double value = rs.getDouble("SUM(VALUE)");
					String date = rs.getString("DATE");
					String comment = rs.getString("COMMENT");
					double roundOff = Math.round(value * 100.0) / 100.0;
					outcome = new Outcome(id, categoryId, expenseId, roundOff, Date.valueOf(date), comment);
					list.add(outcome);
				}
				conn.commit();
				rs.close();
			} catch (Exception e) {
				Stacktrace.print(e);
			}
		}
		return list;
	}

	public double getTotalByMonthAndYear(int month, int year) {
		double sum = 0.0;
		if (year != 0) {
			PreparedStatement preparedStatement;
			ResultSet rs = null;
			String query = "SELECT SUM(VALUE) FROM OUTCOME WHERE strftime('%m', DATE) = '"
					+ DateUtil.zeroPadMonth(month) + "' AND STRFTIME('%Y', DATE) = '" + year + "'";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					sum = rs.getDouble("SUM(VALUE)");
				}
				conn.commit();
				rs.close();
			} catch (Exception e) {
				Stacktrace.print(e);
			}
		}
		return sum;
	}
	
	public double getTotalByYear(int year) {
		double sum = 0.0;
		if (year != 0) {
			PreparedStatement preparedStatement;
			ResultSet rs = null;
			String query = "SELECT SUM(VALUE) FROM OUTCOME WHERE STRFTIME('%Y', DATE) = '" + year + "'";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					sum = rs.getDouble("SUM(VALUE)");
				}
				conn.commit();
				rs.close();
			} catch (Exception e) {
				Stacktrace.print(e);
			}
		}
		return sum;
	}
	
	public List<SumModel> getAllYears() {
		List<SumModel> yearList = new ArrayList<>();

		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "SELECT STRFTIME('%Y', DATE) AS year FROM OUTCOME GROUP BY year ORDER BY year DESC";
		try {
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				yearList.add(new SumModel(rs.getString("year"), ""));
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return yearList;
	}
	
	public List<SumModel> getSumOfAllYears() {
		List<SumModel> oldSumList = getAllYears();
		List<SumModel> sumList = new ArrayList<>();
		double income = 0;
		double outcome = 0;
		
		for (SumModel year : oldSumList) {
			PreparedStatement preparedStatement;
			ResultSet rs = null;
			String query = "SELECT SUM(VALUE) as sum FROM OUTCOME WHERE STRFTIME('%Y', DATE) = '" + year.getYear() + "'";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					outcome = rs.getDouble("sum");
				}
				conn.commit();
				rs.close();
			} catch (Exception e) {
				Stacktrace.print(e);
			}
			
			query = "SELECT SUM(VALUE) as sum FROM INCOME WHERE STRFTIME('%Y', DATE) = '" + year.getYear() + "'";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					income = rs.getDouble("sum");
				}
				conn.commit();
				rs.close();
			} catch (Exception e) {
				Stacktrace.print(e);
			}
			
			year.setSum(String.format("%.2f", (income - outcome)));
			sumList.add(year);
		}
		
		return sumList;
	}

	public void save(Outcome outcome) {
		Statement statement;
		try {
			statement = conn.createStatement();

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date formattedDate = format.parse(outcome.getDate());
			Date sqlDate = new Date(formattedDate.getTime());
			
			statement.executeUpdate("INSERT INTO OUTCOME (EXPENSE_ID, CATEGORY_ID,VALUE, DATE, COMMENT)" + "VALUES ("
					+ outcome.getExpenseId() + "," + outcome.getCategoryId() + "," + outcome.getValue() + "," + "'"
					+ sqlDate + "','" + outcome.getComment() + "')");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void update(Outcome outcome) {
		Statement statement;
		try {
			statement = conn.createStatement();

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date formattedDate = format.parse(outcome.getDate());
			Date sqlDate = new Date(formattedDate.getTime());
			
			statement.executeUpdate("UPDATE OUTCOME SET EXPENSE_ID = "+ outcome.getExpenseId() +", CATEGORY_ID = " + 
			outcome.getCategoryId()+", VALUE = " + outcome.getValue() + ", DATE = '" + sqlDate + "', COMMENT = '"+
			outcome.getComment()+"' WHERE ID = " + outcome.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}
	
	public void changeCategory(Expense expense) {
		Statement statement;
		try {
			statement = conn.createStatement();
			
			statement.executeUpdate("UPDATE OUTCOME SET CATEGORY_ID = " + 
					expense.getCategoryId() +" WHERE EXPENSE_ID = " + expense.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

	public void delete(Outcome outcome) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM OUTCOME WHERE ID = " + outcome.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

}
