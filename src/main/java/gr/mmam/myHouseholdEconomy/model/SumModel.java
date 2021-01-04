package gr.mmam.myHouseholdEconomy.model;

public class SumModel {
	
	String year;
	String sum;
	
	public SumModel(String year, String sum) {
		this.year = year;
		this.sum = sum;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "SumModel [year=" + year + ", sum=" + sum + "]";
	}
	
	

}
