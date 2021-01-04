package gr.mmam.myHouseholdEconomy.model;

import java.sql.Date;

public class Income {

	int id;
	int categoryId;
	int concernsId;
	double value;
	int taxed;
	String comment;
	int coupon;
	Date date;

	public Income(int id, int categoryId, int concernsId, double value, int taxed, String comment, int coupon, Date date) {
		super();
		this.id = id;
		this.categoryId = categoryId;
		this.concernsId = concernsId;
		this.value = value;
		this.taxed = taxed;
		this.comment = comment;
		this.coupon = coupon;
		this.date = date;
	}

	public Income(int categoryId, int concernsId, double value, int taxed, String comment, int coupon, Date date) {
		super();
		this.categoryId = categoryId;
		this.concernsId = concernsId;
		this.value = value;
		this.taxed = taxed;
		this.comment = comment;
		this.coupon = coupon;
		this.date = date;
	}
	
	public Income(int categoryId, int concernsId, double value, boolean taxed, String comment, boolean coupon, Date date) {
		super();
		if (taxed) {
			this.taxed = 1;
		} else {
			this.taxed = 0;
		}
		if (coupon) {
			this.coupon = 1;
		} else {
			this.coupon = 0;
		}
		this.categoryId = categoryId;
		this.concernsId = concernsId;
		this.value = value;
		this.comment = comment;
		this.date = date;
	}
	
	public Income(int id, int categoryId, int concernsId, double value, boolean taxed, String comment, boolean coupon, Date date) {
		super();
		this.id = id;
		if (taxed) {
			this.taxed = 1;
		} else {
			this.taxed = 0;
		}
		if (coupon) {
			this.coupon = 1;
		} else {
			this.coupon = 0;
		}
		this.categoryId = categoryId;
		this.concernsId = concernsId;
		this.value = value;
		this.comment = comment;
		this.date = date;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getConcernsId() {
		return concernsId;
	}

	public void setConcernsId(int concernsId) {
		this.concernsId = concernsId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getTaxed() {
		return taxed;
	}

	public void setTaxed(int taxed) {
		this.taxed = taxed;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryId;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + concernsId;
		result = prime * result + coupon;
		result = prime * result + id;
		result = prime * result + taxed;
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
		Income other = (Income) obj;
		if (categoryId != other.categoryId)
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (concernsId != other.concernsId)
			return false;
		if (coupon != other.coupon)
			return false;
		if (id != other.id)
			return false;
		if (taxed != other.taxed)
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Income [id=" + id + ", categoryId=" + categoryId + ", concernsId=" + concernsId + ", value=" + value
				+ ", taxed=" + taxed + ", comment=" + comment + ", coupon=" + coupon + "]";
	}

}
