package gr.mmam.myHouseholdEconomy.view;

import java.util.List;

import gr.mmam.myHouseholdEconomy.MainApp;
import gr.mmam.myHouseholdEconomy.dao.CapitalDao;
import gr.mmam.myHouseholdEconomy.dao.ExpenseCategoryDAO;
import gr.mmam.myHouseholdEconomy.dao.ExpenseDAO;
import gr.mmam.myHouseholdEconomy.dao.OptionsDao;
import gr.mmam.myHouseholdEconomy.dao.OutcomeDao;
import gr.mmam.myHouseholdEconomy.model.Outcome;
import gr.mmam.myHouseholdEconomy.model.SumModel;
import gr.mmam.myHouseholdEconomy.util.DateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class StatisticsController {
	
//	@FXML
//	Pane statisticsPane = new Pane();
//	
//	@FXML
//	Label titleLbl = new Label();
	
	@FXML
	Button yearExpensePlusBtn = new Button();
	
	@FXML
	Button yearExpenseMinusBtn = new Button();
	
	@FXML
	Button monthExpensePlusBtn = new Button();
	
	@FXML
	Button monthExpenseMinusBtn = new Button();
	
	@FXML
	Label yearExpenseLbl = new Label();
	
	@FXML
	Label monthExpenseLbl = new Label();
	
	@FXML
	private TableView<Outcome> expenseYearTable;

	@FXML
	private TableView<Outcome> expenseMonthTable;

	@FXML
	private TableView<SumModel> sumYearTable;
	
	@FXML
	private TableColumn<Outcome, String> colMonthExpensesAmount;

	@FXML
	private TableColumn<Outcome, String> colMonthExpensesName;

	@FXML
	private TableColumn<Outcome, String> colYearExpensesAmount;

	@FXML
	private TableColumn<Outcome, String> colYearExpensesName;

	@FXML
	private TableColumn<SumModel, String> colSumYearAmount;

	@FXML
	private TableColumn<SumModel, String> colSumYearName;
	
	private ObservableList<Outcome> monthData;
	private ObservableList<Outcome> yearData;
	private ObservableList<SumModel> sumData;
	
	OutcomeDao outcomeDao = new OutcomeDao();
	ExpenseDAO expenseDao = new ExpenseDAO();
	CapitalDao capitalDao = new CapitalDao();
	OptionsDao optionsDao = new OptionsDao();
	ExpenseCategoryDAO expenseCategoryDAO = new ExpenseCategoryDAO();
	
	int month, year;
	
	MainController mainController = new MainController();
	
	private MainApp mainApp;

	public StatisticsController() {
	}

	@FXML
	public void initialize() {
		initializeTexts();
		initializeMonthExpenses(month, year);
		initializeYearExpenses(year);
		initializeSumYear();
		
		expenseYearTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		expenseMonthTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		sumYearTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		yearExpensePlusBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String tmpYear = yearExpenseLbl.getText().replace("Expenses of Year ", "");
				initializeTexts(Integer.parseInt(tmpYear) + 1,month, "Year");
				initializeYearExpenses(Integer.parseInt(tmpYear) + 1);
			}
		});
		
		yearExpenseMinusBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String tmpYear = yearExpenseLbl.getText().replace("Expenses of Year ", "");
				initializeTexts(Integer.parseInt(tmpYear) - 1, month, "Year");
				initializeYearExpenses(Integer.parseInt(tmpYear) - 1);
			}
		});
		
		monthExpensePlusBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String tmpYear = monthExpenseLbl.getText().replace("Expenses of ", "").split(" ")[1];
				String tmpMonth = monthExpenseLbl.getText().replace("Expenses of ", "").split(" ")[0];
				initializeTexts(Integer.parseInt(tmpYear), DateUtil.getMonthIndexFromString(tmpMonth) + 1, "Month");
				tmpYear = monthExpenseLbl.getText().replace("Expenses of ", "").split(" ")[1];
				initializeMonthExpenses(DateUtil.getMonthIndexFromString(tmpMonth) + 1, Integer.parseInt(tmpYear));
			}
		});
		
		monthExpenseMinusBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String tmpYear = monthExpenseLbl.getText().replace("Expenses of ", "").split(" ")[1];
				String tmpMonth = monthExpenseLbl.getText().replace("Expenses of ", "").split(" ")[0];
				initializeTexts(Integer.parseInt(tmpYear), DateUtil.getMonthIndexFromString(tmpMonth) - 1, "Month");
				tmpYear = monthExpenseLbl.getText().replace("Expenses of ", "").split(" ")[1];
				initializeMonthExpenses(DateUtil.getMonthIndexFromString(tmpMonth) - 1, Integer.parseInt(tmpYear));
			}
		});
	}
	
	public void initializeMonthExpenses(int month, int year) {
		monthData = FXCollections.observableArrayList();
		if (month < 0) {
			month = 12 + month;
		} else if (month > 11) {
			month = 12 - month;
		}
		List<Outcome> expenseList = outcomeDao.findByMonthAndYear(month + 1, year);
		for (int i = 0; i < expenseList.size(); i++) {
			monthData.add(expenseList.get(i));
		}

			colMonthExpensesAmount.setCellValueFactory(new PropertyValueFactory<Outcome, String>("value"));
			colMonthExpensesName.setCellValueFactory(new Callback<CellDataFeatures<Outcome, String>, ObservableValue<String>>() {

				public ObservableValue<String> call(CellDataFeatures<Outcome, String> param) {
					int catId = param.getValue().getExpenseId();
					String catName = expenseDao.findById(catId).getName();
					return new SimpleStringProperty(catName);
				}
			});

			expenseMonthTable.setItems(null);
			expenseMonthTable.setItems(monthData);
	}
	
	public void initializeYearExpenses(int year) {

		yearData = FXCollections.observableArrayList();
		List<Outcome> expenseList = outcomeDao.findByYearByValue(year);
		for (int i = 0; i < expenseList.size(); i++) {
			yearData.add(expenseList.get(i));
		}

			colYearExpensesAmount.setCellValueFactory(new PropertyValueFactory<Outcome, String>("value"));
			colYearExpensesName.setCellValueFactory(new Callback<CellDataFeatures<Outcome, String>, ObservableValue<String>>() {

				public ObservableValue<String> call(CellDataFeatures<Outcome, String> param) {
					int catId = param.getValue().getExpenseId();
					String catName = expenseDao.findById(catId).getName();
					return new SimpleStringProperty(catName);
				}
			});
			
			expenseYearTable.setItems(null);
			expenseYearTable.setItems(yearData);
	}
	
	public void initializeSumYear() {
		sumData = FXCollections.observableArrayList();
		List<SumModel> sumList = outcomeDao.getSumOfAllYears();
		
		for (int i = 0; i < sumList.size(); i++) {
			sumData.add(sumList.get(i));
		}

			colSumYearName.setCellValueFactory(new PropertyValueFactory<SumModel, String>("year"));
			colSumYearAmount.setCellValueFactory(new PropertyValueFactory<SumModel, String>("sum"));
			
			sumYearTable.setItems(null);
			sumYearTable.setItems(sumData);
	}
	
	public void initializeTexts() {
		yearExpenseLbl.setText(yearExpenseLbl.getText().replace("$Year", String.valueOf(DateUtil.getYear())));
		monthExpenseLbl.setText(monthExpenseLbl.getText().replace("$Month", String.valueOf(DateUtil.getMonth())).replace("$Year", String.valueOf(DateUtil.getYear())));
	}
	
	public void initializeTexts(int year, int month, String table) {
		String tmpYear = yearExpenseLbl.getText().replace("Expenses of Year ", "");
		String tmpYearFromMonth = monthExpenseLbl.getText().replace("Expenses of ", "").split(" ")[1];
		String tmpMonth = monthExpenseLbl.getText().replace("Expenses of ", "").split(" ")[0];
		if (month > 11 && DateUtil.getMonthIndexFromString(tmpMonth) < month) {
			year = year + 1;
		} else if(month > 11 && DateUtil.getMonthIndexFromString(tmpMonth) > month) {
			year = year - 1;
		}
		
		if (month < 0) {
			month = 12 + month;
			year = year -1;
		}
		if (table.equals("Month")) {
			monthExpenseLbl.setText(monthExpenseLbl.getText().replace(tmpMonth, DateUtil.getMonthForInt(month)).replace(tmpYearFromMonth, String.valueOf(year)));
		} else if(table.equals("Year")) {
			yearExpenseLbl.setText(yearExpenseLbl.getText().replace(tmpYear, String.valueOf(year)));
		}
		mainApp.getPrimaryStage().show();
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
