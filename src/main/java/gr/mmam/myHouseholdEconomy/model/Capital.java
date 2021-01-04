package gr.mmam.myHouseholdEconomy.model;

import java.sql.Date;

public class Capital {

	int id;
	double value;
	String comment;
	Date date;
	double checkpoint;
	
	public Capital(double value, String comment, Date date, double checkpoint) {
		super();
		this.value = value;
		this.comment = comment;
		this.date = date;
		this.checkpoint = checkpoint;
	}
	
	public Capital(int id, double value, String comment, String date, double checkpoint) {
		super();
		this.id = id;
		this.value = value;
		this.comment = comment;
		this.date = Date.valueOf(date);
		this.checkpoint = checkpoint;
	}

	public double getCheckpoint() {
		return checkpoint;
	}

	public void setCheckpoint(double checkpoint) {
		this.checkpoint = checkpoint;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(checkpoint);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
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
		Capital other = (Capital) obj;
		if (Double.doubleToLongBits(checkpoint) != Double.doubleToLongBits(other.checkpoint))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Capital [id=" + id + ", value=" + value + ", comment=" + comment + ", date=" + date + ", checkpoint="
				+ checkpoint + "]";
	}

}
