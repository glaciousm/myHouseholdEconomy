package gr.mmam.myHouseholdEconomy.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gr.mmam.myHouseholdEconomy.db.SqliteConnection;
import gr.mmam.myHouseholdEconomy.model.Income;
import gr.mmam.myHouseholdEconomy.util.DateUtil;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;

public class IncomeDao {

	Connection conn;
	Income income;

	public IncomeDao() {
		conn = SqliteConnection.Connect();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Stacktrace.print(e);
		}
	}

	public List<Income> getList() {
		List<Income> list = new ArrayList<Income>();
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from INCOME ORDER BY CATEGORY_ID ASC";
		try {
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				int concernsId = rs.getInt("CONCERNS_ID");
				int categoryId = rs.getInt("CATEGORY_ID");
				double value = rs.getDouble("VALUE");
				int taxed = rs.getInt("ISTAXED");
				String comment = rs.getString("COMMENT");
				int coupon = rs.getInt("ISCOUPON");
				Date date = rs.getDate("DATE");
				income = new Income(id, categoryId, concernsId, value, taxed, comment, coupon, date);
				list.add(income);
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return list;
	}

	public Income findById(int id) {
		PreparedStatement preparedStatement;
		ResultSet rs = null;
		String query = "Select * from INCOME WHERE id = " + id + " ORDER BY CATEGORY_ID ASC";
		try {
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int concernsId = rs.getInt("CONCERNS_ID");
				int categoryId = rs.getInt("CATEGORY_ID");
				double value = rs.getDouble("VALUE");
				int taxed = rs.getInt("ISTAXED");
				String comment = rs.getString("COMMENT");
				int coupon = rs.getInt("ISCOUPON");
				Date date = rs.getDate("DATE");
				income = new Income(id, categoryId, concernsId, value, taxed, comment, coupon, date);
			}
			conn.commit();
			rs.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
		return income;
	}

	public List<Income> findByMonthAndYear(int month, int year) {
		List<Income> list = new ArrayList<Income>();
		if (year != 0) {
			PreparedStatement preparedStatement;
			ResultSet rs = null;
			String query = "SELECT * FROM INCOME WHERE strftime('%m', DATE) = '" + DateUtil.zeroPadMonth(month)
					+ "' AND STRFTIME('%Y', DATE) = '" + year + "'" + " ORDER BY CATEGORY_ID ASC";
			try {
				preparedStatement = conn.prepareStatement(query);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("ID");
					int concernsId = rs.getInt("CONCERNS_ID");
					int categoryId = rs.getInt("CATEGORY_ID");
					double value = rs.getDouble("VALUE");
					int taxed = rs.getInt("ISTAXED");
					String comment = rs.getString("COMMENT");
					int coupon = rs.getInt("ISCOUPON");
					String date = rs.getString("DATE");
					income = new Income(id, categoryId, concernsId, value, taxed, comment, coupon, Date.valueOf(date));
					list.add(income);
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
			String query = "SELECT SUM(VALUE) FROM INCOME WHERE strftime('%m', DATE) = '" + DateUtil.zeroPadMonth(month)
					+ "' AND STRFTIME('%Y', DATE) = '" + year + "'";
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
			String query = "SELECT SUM(VALUE) FROM INCOME WHERE STRFTIME('%Y', DATE) = '" + year + "'";
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

	public void save(Income income) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate(
					"INSERT INTO INCOME (CATEGORY_ID, CONCERNS_ID, VALUE, ISTAXED, COMMENT, ISCOUPON, DATE)"
							+ "VALUES (" + income.getCategoryId() + "," + income.getConcernsId() + ","
							+ income.getValue() + "," + income.getTaxed() + ",'" + income.getComment() + "',"
							+ income.getCoupon() + ",'" + income.getDate() + "')");
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

	public void update(Income income) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate(
					"UPDATE INCOME SET CATEGORY_ID =" + income.getCategoryId() + ", CONCERNS_ID =" + income.getConcernsId() +
					", VALUE = " + income.getValue() + ", ISTAXED = " + income.getTaxed() + ", COMMENT = '" + income.getComment() +
					"', ISCOUPON = " + income.getCoupon() + ", DATE = '" + income.getDate() + "' WHERE ID = " + income.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

	public void delete(Income income) {
		Statement statement;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM INCOME WHERE ID = " + income.getId());
			conn.commit();
			statement.close();
		} catch (Exception e) {
			Stacktrace.print(e);
		}
	}

}
