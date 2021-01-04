package gr.mmam.myHouseholdEconomy.view;

import java.util.List;

import gr.mmam.myHouseholdEconomy.MainApp;
import gr.mmam.myHouseholdEconomy.dao.CapitalDao;
import gr.mmam.myHouseholdEconomy.dao.ExpenseCategoryDAO;
import gr.mmam.myHouseholdEconomy.dao.ExpenseDAO;
import gr.mmam.myHouseholdEconomy.dao.IncomeDao;
import gr.mmam.myHouseholdEconomy.dao.OptionsDao;
import gr.mmam.myHouseholdEconomy.dao.OutcomeDao;
import gr.mmam.myHouseholdEconomy.model.Capital;
import gr.mmam.myHouseholdEconomy.model.Outcome;
import gr.mmam.myHouseholdEconomy.util.DateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class OverviewController {

	CapitalDao capitalDao = new CapitalDao();
	OptionsDao optionsDao = new OptionsDao();
	ExpenseCategoryDAO expenseCategoryDAO = new ExpenseCategoryDAO();

	int month, year;
	
	String currency = optionsDao.getCurrency();
	
	@FXML
	Pane monthlyHouseholdPane = new Pane();

	@FXML
	Label monthlyHouseholdLabel = new Label();
	
	@FXML
	Label balanceLabel = new Label();
	
	@FXML
	Pane monthlyTopPane = new Pane();

	@FXML
	Label monthlyTopLabel = new Label();
	
	@FXML
	Pane yearlyTopPane = new Pane();

	@FXML
	Label yearlyTopLabel = new Label();
	
	@FXML
	Label monthIncomeLabel = new Label();
	
	@FXML
	Label monthExpensesLabel = new Label();
	
	@FXML
	Label monthTotalLabel = new Label();
	
	@FXML
	Pane yearlyHouseholdPane = new Pane();

	@FXML
	Label yearlyHouseholdLabel = new Label();
	
	@FXML
	Label yearIncomeLabel = new Label();
	
	@FXML
	Label yearExpensesLabel = new Label();
	
	@FXML
	Label yearTotalLabel = new Label();
	
	@FXML
	Label monthExpenses = new Label();

	@FXML
	Label monthIncome = new Label();

	@FXML
	Label monthTotal = new Label();

	@FXML
	Label yearExpenses = new Label();

	@FXML
	Label yearIncome = new Label();

	@FXML
	Label yearTotal = new Label();

	@FXML
	Label balanceTotal = new Label();

	@FXML
	private TableView<Outcome> expenseYearTable;

	@FXML
	private TableView<Outcome> expenseMonthTable;

	@FXML
	private TableColumn<Outcome, String> colMonthExpensesAmount;

	@FXML
	private TableColumn<Outcome, String> colMonthExpensesName;

	@FXML
	private TableColumn<Outcome, String> colYearExpensesAmount;

	@FXML
	private TableColumn<Outcome, String> colYearExpensesName;

	private ObservableList<Outcome> monthData;
	private ObservableList<Outcome> yearData;

	@SuppressWarnings("unused")
	private MainApp mainApp;
	
	OutcomeDao outcomeDao = new OutcomeDao();
	ExpenseDAO expenseDao = new ExpenseDAO();
	IncomeDao incomeDao = new IncomeDao();

	public OverviewController() {

	}

	@FXML
	public void initialize() {
		System.out.println("initialize Overview");
		yearlyHouseholdLabel.setText("Total for Year " + DateUtil.getYear());
		monthlyHouseholdLabel.setText("Total for Month " + DateUtil.getMonth());
		monthlyTopLabel.setText("Top Expenses of " + DateUtil.getMonth());
		yearlyTopLabel.setText("Top Expenses of " + DateUtil.getYear());
		initializePanes();
		initializeMonthTotal();
		initializeYearTotal(year);
		initializeMonthExpenses();
		initializeYearExpenses();
		initializeBalance();
	}
	
	public double initializeMonthTotal() {
		Double expensesTotal = 0.0;
		Double incomeTotal = 0.0;
		Double total = 0.0;
		expensesTotal = outcomeDao.getTotalByMonthAndYear(month, year);
		incomeTotal = incomeDao.getTotalByMonthAndYear(month, year);
		total = incomeTotal - expensesTotal;
		if (total > 0) {
			monthTotal.setStyle("-fx-background-color: GREEN;");
		} else if (total < 0) {
			monthTotal.setStyle("-fx-background-color: RED;");
		} else {
			monthTotal.setStyle("-fx-background-color: YELLOW;");
		}
		double roundOff = Math.round(total * 100.0) / 100.0;
		monthExpenses.setText(String.format("%.2f", expensesTotal) +" " + currency);
		monthIncome.setText(String.format("%.2f", incomeTotal) +" " + currency);
		monthTotal.setText(String.format("%.2f", roundOff) +" " + currency);
		return roundOff;
	}

	public void initializeMonthExpenses() {

		monthData = FXCollections.observableArrayList();
		List<Outcome> expenseList = outcomeDao.findByMonthAndYearByValueTopFive(month, year);
		for (int i = 0; i < expenseList.size(); i++) {
			monthData.add(expenseList.get(i));
		}

			colMonthExpensesAmount.setCellValueFactory(new PropertyValueFactory<Outcome, String>("value"));
			colMonthExpensesName.setCellValueFactory(new Callback<CellDataFeatures<Outcome, String>, ObservableValue<String>>() {

				public ObservableValue<String> call(CellDataFeatures<Outcome, String> param) {
					int catId = param.getValue().getCategoryId();
					String catName = expenseCategoryDAO.findById(catId).getName();
					return new SimpleStringProperty(catName);
				}
			});

			expenseMonthTable.setItems(null);
			expenseMonthTable.setItems(monthData);
	}
	
	public void initializeYearExpenses() {

		yearData = FXCollections.observableArrayList();
		List<Outcome> expenseList = outcomeDao.findByYearByValueTopFive(year);
		for (int i = 0; i < expenseList.size(); i++) {
			yearData.add(expenseList.get(i));
		}

			colYearExpensesAmount.setCellValueFactory(new PropertyValueFactory<Outcome, String>("value"));
			colYearExpensesName.setCellValueFactory(new Callback<CellDataFeatures<Outcome, String>, ObservableValue<String>>() {

				public ObservableValue<String> call(CellDataFeatures<Outcome, String> param) {
					int catId = param.getValue().getCategoryId();
					String catName = expenseCategoryDAO.findById(catId).getName();
					return new SimpleStringProperty(catName);
				}
			});

			expenseYearTable.setItems(null);
			expenseYearTable.setItems(yearData);
	}

	public double initializeYearTotal(int year) {
		Double expensesTotal = 0.0;
		Double incomeTotal = 0.0;
		Double total = 0.0;
		expensesTotal = outcomeDao.getTotalByYear(year);
		incomeTotal = incomeDao.getTotalByYear(year);
		total = incomeTotal - expensesTotal;
		double roundOff = Math.round(total * 100.0) / 100.0;
		if (total > 0) {
			yearTotal.setStyle("-fx-background-color: GREEN;");
		} else if (total < 0) {
			yearTotal.setStyle("-fx-background-color: RED;");
		} else {
			yearTotal.setStyle("-fx-background-color: YELLOW;");
		}
		yearExpenses.setText(String.format("%.2f", expensesTotal) +" " + currency);
		yearIncome.setText(String.format("%.2f", incomeTotal) +" " + currency);
		yearTotal.setText(String.format("%.2f", roundOff) +" " + currency);
		return roundOff;
	}
	


	public void initializeBalance() {
		Capital capital = capitalDao.getLastCheckValue();
		double balance = 0.0;
		if (capital != null) {
			balance = capital.getValue() - capital.getCheckpoint() + initializeYearTotal(year);
		} else {
			if (initializeYearTotal(DateUtil.getYear()) != 0.0) {
				balance = initializeYearTotal(DateUtil.getYear());
			} else {
				balance = 0.0;
			}
		}
		balanceTotal.setText(String.format("%.2f", balance) +" " + currency);
	}
	
	private void initializeMonthlyHouseholdPane() {
		monthlyHouseholdLabel.layoutXProperty().bind(monthlyHouseholdPane.widthProperty().subtract(monthlyHouseholdLabel.widthProperty()).divide(2));
		monthIncomeLabel.layoutXProperty().bind(monthlyHouseholdPane.widthProperty().subtract(monthlyHouseholdLabel.widthProperty()).divide(2.3));
		monthIncome.layoutXProperty().bind(monthlyHouseholdPane.widthProperty().subtract(monthlyHouseholdLabel.widthProperty()).divide(2.3).multiply(2));
		monthExpensesLabel.layoutXProperty().bind(monthlyHouseholdPane.widthProperty().subtract(monthlyHouseholdLabel.widthProperty()).divide(2.3));
		monthExpenses.layoutXProperty().bind(monthlyHouseholdPane.widthProperty().subtract(monthlyHouseholdLabel.widthProperty()).divide(2.3).multiply(2));
		monthTotalLabel.layoutXProperty().bind(monthlyHouseholdPane.widthProperty().subtract(monthlyHouseholdLabel.widthProperty()).divide(2.3));
		monthTotal.layoutXProperty().bind(monthlyHouseholdPane.widthProperty().subtract(monthlyHouseholdLabel.widthProperty()).divide(2.3).multiply(2));
	}
	
	private void initializeYearlyHouseholdPane() {
		yearlyHouseholdLabel.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2));
		yearIncomeLabel.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2.4));
		yearIncome.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2.4).multiply(2));
		yearExpensesLabel.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2.4));
		yearExpenses.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2.4).multiply(2));
		yearTotalLabel.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2.4));
		yearTotal.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2.4).multiply(2));
		balanceLabel.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2.4));
		balanceTotal.layoutXProperty().bind(yearlyHouseholdPane.widthProperty().subtract(yearlyHouseholdLabel.widthProperty()).divide(2.4).multiply(2));
	}
	
	private void initializeYearlyTopPane() {
		yearlyTopLabel.layoutXProperty().bind(yearlyTopPane.widthProperty().subtract(yearlyTopLabel.widthProperty()).divide(2));
		expenseYearTable.layoutXProperty().bind(yearlyTopPane.widthProperty().subtract(expenseYearTable.widthProperty()).divide(2));
		expenseYearTable.prefHeightProperty().bind(yearlyTopPane.heightProperty().subtract(60.0));
	}
	
	private void initializeMonthlyTopPane() {
		monthlyTopLabel.layoutXProperty().bind(monthlyTopPane.widthProperty().subtract(monthlyTopLabel.widthProperty()).divide(2));
		expenseMonthTable.layoutXProperty().bind(monthlyTopPane.widthProperty().subtract(expenseMonthTable.widthProperty()).divide(2));
		expenseMonthTable.prefHeightProperty().bind(monthlyTopPane.heightProperty().subtract(60.0));
	}
	
	
	private void initializePanes() {
		initializeMonthlyHouseholdPane();
		initializeYearlyHouseholdPane();
		initializeYearlyTopPane();
		initializeMonthlyTopPane();
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public int getYear() {
		return year;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
