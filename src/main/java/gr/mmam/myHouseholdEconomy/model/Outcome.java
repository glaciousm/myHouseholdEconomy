package gr.mmam.myHouseholdEconomy.model;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Outcome {
	
	int id;
	int expenseId;
	int categoryId;
	double value;
	Date date;
	String comment;
	
	public Outcome() {
	}
	
	public Outcome(int id,int expenseId, int categoryId, double value, Date date, String comment) {
		super();
		this.id = id;
		this.expenseId = expenseId;
		this.categoryId = categoryId;
		this.value = value;
		this.date = date;
		this.comment = comment;
	}
	
	public Outcome(int expenseId, int categoryId, double value, Date date, String comment) {
		super();
		this.expenseId = expenseId;
		this.categoryId = categoryId;
		this.value = value;
		this.date = date;
		this.comment = comment;
	}

	public int getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(int expenseId) {
		this.expenseId = expenseId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + categoryId;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + expenseId;
		result = prime * result + id;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Outcome other = (Outcome) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (categoryId != other.categoryId)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (expenseId != other.expenseId)
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Outcome [id=" + id + ", expenseId=" + expenseId + ", categoryId=" + categoryId + ", value=" + value
				+ ", date=" + date + ", comment=" + comment + "]";
	}
	
	
}
