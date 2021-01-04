package gr.mmam.myHouseholdEconomy.model;

public class Expense {
	
	int id;
	String name;
	int categoryId;
	
	public Expense() {
	}
	
	public Expense(String name, ExpenseCategory categoryId) {
		super();
		this.name = name;
		this.categoryId = categoryId.getId();
	}
	
	public Expense(String name, int categoryId) {
		super();
		this.name = name;
		this.categoryId = categoryId;
	}
	
	public Expense(int id, String name, ExpenseCategory categoryId) {
		super();
		this.id = id;
		this.name = name;
		this.categoryId = categoryId.getId();
	}
	
	public Expense(int id, String name, int categoryId) {
		super();
		this.id = id;
		this.name = name;
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryId;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Expense other = (Expense) obj;
		if (categoryId != other.categoryId)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Expense [id=" + id + ", name=" + name + ", categoryId=" + categoryId + "]";
	}
}
