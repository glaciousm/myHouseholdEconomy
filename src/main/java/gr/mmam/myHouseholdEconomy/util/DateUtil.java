package gr.mmam.myHouseholdEconomy.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	
	public static int getMonthNumber() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		return month+1;
	}
	
	public static int getYear() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return year;
	}

	public static String getMonth() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		String monthStr = getMonthForInt(month);

		return monthStr;
	}

	public static int getMonthIndexFromString(String name) {
		Date date = null;
		try {
			date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(name);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);

		return month;
	}

	public static String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols(new Locale("en"));
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		} else {
			month = months[num - 12];
		}
		return month;
	}
	
	public static LocalDate getToday() {
		LocalDate today = LocalDate.now();
		return today;
	}
	
	public static LocalDate getLocalDateFromString(String date) {
		LocalDate localDate = LocalDate.parse(date);
		return localDate;
	}
	
	public static String zeroPadDay(int day) {
		String zeroPadDay = null;
		if (day <10) {
			zeroPadDay = "0" + day;
		} else {
			zeroPadDay = String.valueOf(day);
		}
		return zeroPadDay;
	}
	
	public static String zeroPadMonth(int month) {
		String zeroPadMonth = null;
		if (month <10) {
			zeroPadMonth = "0" + month;
		} else {
			zeroPadMonth = String.valueOf(month);
		}
		return zeroPadMonth;
	}

	public static int getPreviousMonth(int month){
			if (month == 0) {
				month = 11;
			} else {
				month = month - 1;
			}
			return month;
	}

	public static int getAfterMonth(int month){
		int newMonth = 0;
		if (month == 11) {
			newMonth = 0;
		} else {
			newMonth = month + 1;
		}

		return newMonth;
	}

	public static int getPreviousYear(int month, int year){
		int newYear = 0;
		if (month == 0) {
			newYear = year - 1;
		} else {
			newYear = year;
		}

		return newYear;
	}

	public static int getAfterYear(int month, int year){
		int newYear = 0;
		if (month == 11) {
			newYear = year + 1;
		} else {
			newYear = year;
		}

		return newYear;
	}

}
