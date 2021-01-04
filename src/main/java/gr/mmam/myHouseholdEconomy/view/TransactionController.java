package gr.mmam.myHouseholdEconomy.view;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import gr.mmam.myHouseholdEconomy.MainApp;
import gr.mmam.myHouseholdEconomy.dao.ExpenseCategoryDAO;
import gr.mmam.myHouseholdEconomy.dao.ExpenseDAO;
import gr.mmam.myHouseholdEconomy.dao.IncomeCategoryDAO;
import gr.mmam.myHouseholdEconomy.dao.IncomeConcernsDao;
import gr.mmam.myHouseholdEconomy.dao.IncomeDao;
import gr.mmam.myHouseholdEconomy.dao.OptionsDao;
import gr.mmam.myHouseholdEconomy.dao.OutcomeDao;
import gr.mmam.myHouseholdEconomy.model.Expense;
import gr.mmam.myHouseholdEconomy.model.ExpenseCategory;
import gr.mmam.myHouseholdEconomy.model.Income;
import gr.mmam.myHouseholdEconomy.model.IncomeCategory;
import gr.mmam.myHouseholdEconomy.model.IncomeConcerns;
import gr.mmam.myHouseholdEconomy.model.Outcome;
import gr.mmam.myHouseholdEconomy.util.DateUtil;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class TransactionController<JFXButton> {

	MainController mainController = new MainController();

	OutcomeDao outcomeDao = new OutcomeDao();
	ExpenseDAO expenseDao = new ExpenseDAO();
	ExpenseCategoryDAO expenseCategoryDao = new ExpenseCategoryDAO();
	IncomeDao incomeDao = new IncomeDao();
	IncomeConcernsDao incomeConcernsDao = new IncomeConcernsDao();
	IncomeCategoryDAO incomeCategoryDao = new IncomeCategoryDAO();
	OptionsDao optionDao = new OptionsDao();

	@FXML
	private Pane expensePane = new Pane();

	@FXML
	private Pane incomePane = new Pane();

	@FXML
	private Button left = new Button();

	@FXML
	private Button right = new Button();

	@FXML
	private Text curMonth = new Text();

	@FXML
	private Text curYear = new Text();

	@FXML
	private TableView<Outcome> expenseTableSmall;

	@FXML
	private TableView<Income> incomeTable;

	@FXML
	private TableColumn<Outcome, String> colAmountSmall;

	@FXML
	private TableColumn<Outcome, String> colCommentSmall;

	@FXML
	private TableColumn<Outcome, Date> colDateSmall;

	@FXML
	private TableColumn<Income, String> colAmountIncome;

	@FXML
	private TableColumn<Income, String> colCommentIncome;

	@FXML
	private TableColumn<Income, String> colNameIncome;

	@FXML
	private TableColumn<Income, String> colConcernsIncome;

	@FXML
	private TableColumn<Income, String> colTaxedIncome;

	@FXML
	private TableColumn<Income, String> colCouponIncome;

	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn colCategorySmall;

	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn colExpenseSmall;

	List<String> expenseList = new ArrayList<String>();
	List<Outcome> outcomeList = new ArrayList<Outcome>();
	List<Income> incomeList = new ArrayList<Income>();
	List<IncomeCategory> incomeItemList = new ArrayList<IncomeCategory>();
	List<IncomeConcerns> concernsList = new ArrayList<IncomeConcerns>();
	ObservableList<String> observableList = FXCollections.observableArrayList();
	
	String currency = optionDao.getCurrency();

	@FXML
	Label incomeLb = new Label();

	@FXML
	Label expenseLb = new Label();

	@FXML
	Label totalLb = new Label();

	@FXML
	Label incomeTitleLb = new Label();

	@FXML
	Label expenseTitleLb = new Label();

	@FXML
	Button submitBtn = new Button();

	@FXML
	Button deleteBtn = new Button();

	@FXML
	Button editExpenseBtn = new Button();

	@FXML
	Button submitIncomeBtn = new Button();

	@FXML
	Button editIncomeBtn = new Button();

	@FXML
	Button deleteIncomeBtn = new Button();

	@SuppressWarnings("unused")
	private MainApp mainApp;

	public int month;
	public int year;

	private List<String> categoryList = new ArrayList<String>();
	private ObservableList<Outcome> outcomeData;
	private ObservableList<Income> incomeData;

	public TransactionController(int month, int year) {

	}

	public TransactionController() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	public void initialize() {
		getExpenses(month, year);
		getIncome(month, year);
		initializeTotal();
		initializePanes();
		initializeCurMonth();
		deleteBtn.setDisable(true);
		deleteIncomeBtn.setDisable(true);
		editExpenseBtn.setDisable(true);
		editIncomeBtn.setDisable(true);
		expenseTableSmall.setRowFactory(tv -> {
			TableRow<Outcome> row = new TableRow<Outcome>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					deleteBtn.setDisable(false);
					editExpenseBtn.setDisable(false);
				} else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
					editExpensePopup(expenseTableSmall.getSelectionModel().getSelectedItem());
					expenseTableSmall.getSelectionModel().clearSelection();
					deleteBtn.setDisable(true);
					editExpenseBtn.setDisable(true);
				} else {
					deleteBtn.setDisable(true);
					editExpenseBtn.setDisable(true);
				}
			});
			return row;
		});
		incomeTable.setRowFactory(tv -> {
			TableRow<Income> row = new TableRow<Income>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					deleteIncomeBtn.setDisable(false);
					editIncomeBtn.setDisable(false);
				} else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
					editIncomePopup(incomeTable.getSelectionModel().getSelectedItem());
					incomeTable.getSelectionModel().clearSelection();
					deleteIncomeBtn.setDisable(true);
					editIncomeBtn.setDisable(true);
				} else {
					deleteIncomeBtn.setDisable(true);
					editIncomeBtn.setDisable(true);
				}
			});
			return row;
		});
		left.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				int newMonth = DateUtil.getPreviousMonth(DateUtil.getMonthIndexFromString(curMonth.getText()));
				int newYear = DateUtil.getPreviousYear(DateUtil.getMonthIndexFromString(curMonth.getText()), Integer.valueOf(curYear.getText()));
				System.out.println(newMonth + " " + newYear);
				changeDate(newMonth,newYear);
				getExpenses(newMonth + 1, newYear);
				getIncome(newMonth + 1, newYear);
			}
		});
		right.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				int newMonth = DateUtil.getAfterMonth(DateUtil.getMonthIndexFromString(curMonth.getText()));
				int newYear = DateUtil.getAfterYear(DateUtil.getMonthIndexFromString(curMonth.getText()), Integer.valueOf(curYear.getText()));
				System.out.println(newMonth + " " + newYear);
				changeDate(newMonth,newYear);
				getExpenses(newMonth + 1, newYear);
				getIncome(newMonth + 1, newYear);
			}
		});
		submitBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addExpensePopup();
			}
		});
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (expenseTableSmall.getSelectionModel().getSelectedIndex() != -1) {
					Alert alert = new Alert(AlertType.CONFIRMATION,
							"Are you se you want to delete Expense \""
									+ expenseDao.findById(
											expenseTableSmall.getSelectionModel().getSelectedItem().getExpenseId()).getName()
									+ "\" ?",
							ButtonType.YES, ButtonType.CANCEL);
					alert.setHeaderText(null);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.YES) {
						removeFromExpenses(expenseTableSmall.getSelectionModel().getSelectedItem());
					}
				} else {
					Alert alert = new Alert(AlertType.WARNING, "Select an item from the list in order to delete it");
					alert.setHeaderText(null);
					alert.showAndWait();
				}
			}
		});

		deleteIncomeBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (incomeTable.getSelectionModel().getSelectedIndex() != -1) {
					Alert alert = new Alert(AlertType.CONFIRMATION, "Are you se you want to delete selected Income ?",
							ButtonType.YES, ButtonType.CANCEL);
					alert.setHeaderText(null);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.YES) {
						removeFromIncome(incomeTable.getSelectionModel().getSelectedItem());
					}
				} else {
					Alert alert = new Alert(AlertType.WARNING, "Select an item from the list in order to delete it");
					alert.setHeaderText(null);
					alert.showAndWait();
				}
			}
		});

		submitIncomeBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addIncomePopup();
			}
		});
		editIncomeBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				editIncomePopup(incomeTable.getSelectionModel().getSelectedItem());
			}
		});
		editExpenseBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				editExpensePopup(expenseTableSmall.getSelectionModel().getSelectedItem());
			}
		});

	}

	@SuppressWarnings("unchecked")
	public void getExpenses(int month, int year) {
		outcomeList.clear();
		outcomeList = outcomeDao.findByMonthAndYear(month, year);
		outcomeData = FXCollections.observableArrayList();
		if (outcomeList != null) {
			for (Outcome outcome : outcomeList) {
				outcomeData.add(outcome);
			}
			colAmountSmall.setCellValueFactory(new PropertyValueFactory<Outcome, String>("value"));
			colDateSmall.setCellValueFactory(new PropertyValueFactory<Outcome, Date>("date"));
			colCommentSmall.setCellValueFactory(new PropertyValueFactory<Outcome, String>("comment"));
			colCategorySmall
					.setCellValueFactory(new Callback<CellDataFeatures<Outcome, String>, ObservableValue<String>>() {

						public ObservableValue<String> call(CellDataFeatures<Outcome, String> param) {
							return new SimpleStringProperty(
									expenseCategoryDao.findById(param.getValue().getCategoryId()).getName());
						}
					});
			colExpenseSmall
					.setCellValueFactory(new Callback<CellDataFeatures<Outcome, String>, ObservableValue<String>>() {

						public ObservableValue<String> call(CellDataFeatures<Outcome, String> param) {
							return new SimpleStringProperty(
									expenseDao.findById(param.getValue().getExpenseId()).getName());
						}
					});
			expenseTableSmall.setItems(null);
			expenseTableSmall.setItems(outcomeData);
		} else {
			expenseTableSmall.setItems(null);
		}

	}

	public void getIncome(int month, int year) {
		incomeList.clear();
		incomeData = FXCollections.observableArrayList();
		incomeList = incomeDao.findByMonthAndYear(month, year);
		if (incomeList != null) {
			for (Income income : incomeList) {
				incomeData.add(income);
			}
			colAmountIncome.setCellValueFactory(new PropertyValueFactory<Income, String>("value"));
			colCommentIncome.setCellValueFactory(new PropertyValueFactory<Income, String>("comment"));
			colConcernsIncome
					.setCellValueFactory(new Callback<CellDataFeatures<Income, String>, ObservableValue<String>>() {

						public ObservableValue<String> call(CellDataFeatures<Income, String> param) {
							return new SimpleStringProperty(
									incomeConcernsDao.findById(param.getValue().getConcernsId()).getName());
						}
					});
			colNameIncome
					.setCellValueFactory(new Callback<CellDataFeatures<Income, String>, ObservableValue<String>>() {

						public ObservableValue<String> call(CellDataFeatures<Income, String> param) {
							return new SimpleStringProperty(
									incomeCategoryDao.findById(param.getValue().getCategoryId()).getName());
						}
					});
			colTaxedIncome
					.setCellValueFactory(new Callback<CellDataFeatures<Income, String>, ObservableValue<String>>() {

						public ObservableValue<String> call(CellDataFeatures<Income, String> param) {
							String taxed = "No";
							if (param.getValue().getTaxed() == 1) {
								taxed = "Yes";
							}
							return new SimpleStringProperty(taxed);
						}
					});
			colCouponIncome
					.setCellValueFactory(new Callback<CellDataFeatures<Income, String>, ObservableValue<String>>() {

						public ObservableValue<String> call(CellDataFeatures<Income, String> param) {
							String coupon = "No";
							if (param.getValue().getCoupon() == 1) {
								coupon = "Yes";
							}
							return new SimpleStringProperty(coupon);
						}
					});

			incomeTable.setItems(null);
			incomeTable.setItems(incomeData);
		} else {

			incomeTable.setItems(null);
		}

	}

	private void initializeCategory(ChoiceBox<String> categoryCb) {
		categoryList.clear();
		categoryCb.getItems().clear();
		for (ExpenseCategory categoryItem : expenseCategoryDao.getList()) {
			categoryList.add(categoryItem.getName());
		}
		Collections.sort(categoryList);
		for (String categoryItem : categoryList) {
			categoryCb.getItems().add(categoryItem);
		}

	}

	private void initializeExpenseCbByCategory(String categoryName, ChoiceBox<String> expenseCb) {
		expenseCb.getItems().clear();
		for (Expense category : expenseDao.findByCategoryId(expenseCategoryDao.findByName(categoryName).getId())) {
			expenseCb.getItems().add(category.getName());
		}
	}

	private void initializeExpenseCb(ChoiceBox<String> expenseCb) {
		expenseList.clear();
		expenseCb.getItems().clear();
		for (Expense expenseItem : expenseDao.getList()) {
			expenseList.add(expenseItem.getName());
		}
		Collections.sort(expenseList);
		for (String expenseItem : expenseList) {
			expenseCb.getItems().add(expenseItem);
		}

	}

	private void initializeDatePicker(DatePicker datePicker) {
		LocalDate localDate;
		datePicker.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});
		if (month != DateUtil.getMonthNumber() && month != 0) {
			localDate = LocalDate.of(year, month, 01);
		} else {
			localDate = DateUtil.getToday();
		}

		datePicker.setValue(localDate);

	}

	public void addExpense(String amount, Date date, String expenseName, String catName, String comment) {
		Expense expense = expenseDao.findByName(expenseName);
		outcomeDao
				.save(new Outcome(expense.getId(), expense.getCategoryId(), Double.parseDouble(amount), date, comment));
		getExpenses(month, year);
		initializeTotal();
	};

	public void addIncome(String amount, int month, int year, String incomeName, String convernsName, String comment,
			boolean taxed, boolean coupon) {
		String doubleAmount;
		if (amount.contains(",")) {
			doubleAmount = amount.replace(",", ".");
		} else {
			doubleAmount = amount;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		Date dateSQL = new Date((calendar.getTime().getTime()));

		Income income = new Income(incomeCategoryDao.findByName(incomeName).getId(),
				incomeConcernsDao.findByName(convernsName).getId(), Double.parseDouble(doubleAmount), taxed, comment,
				coupon, dateSQL);
		incomeDao.save(income);
		getIncome(month, year);
		initializeTotal();
	};

	public void editIncome(int id, String amount, Date date, String incomeName, String convernsName, String comment,
			boolean taxed, boolean coupon) {
		String doubleAmount;
		if (amount.contains(",")) {
			doubleAmount = amount.replace(",", ".");
		} else {
			doubleAmount = amount;
		}
		Income income = new Income(id, incomeCategoryDao.findByName(incomeName).getId(),
				incomeConcernsDao.findByName(convernsName).getId(), Double.parseDouble(doubleAmount), taxed, comment,
				coupon, date);
		incomeDao.update(income);
		getIncome(month, year);
		initializeTotal();
	};

	public void editExpense(int id, int expenseId, int categoryId, String value, Date date, String comment) {

		Outcome outcome = new Outcome(id, expenseId, categoryId, Double.parseDouble(value), date, comment);
		outcomeDao.update(outcome);
		getExpenses(month, year);
		initializeTotal();
	};

	public void removeFromExpenses(Outcome outcome) {
		outcomeDao.delete(outcome);
		expenseList.clear();
		initializeTotal();
//		getIncome(monthDropdown.getSelectionModel().getSelectedIndex() + 1, Integer.valueOf(yearDropdown.getSelectionModel().getSelectedItem()));
//		getExpenses(monthDropdown.getSelectionModel().getSelectedIndex() + 1, Integer.valueOf(yearDropdown.getSelectionModel().getSelectedItem()));
	}

	public void removeFromIncome(Income income) {
		incomeList.clear();
		incomeDao.delete(income);
		initializeTotal();
//		getIncome(monthDropdown.getSelectionModel().getSelectedIndex() + 1, Integer.valueOf(yearDropdown.getSelectionModel().getSelectedItem()));
//		getExpenses(monthDropdown.getSelectionModel().getSelectedIndex() + 1, Integer.valueOf(yearDropdown.getSelectionModel().getSelectedItem()));
	}

	private void addIncomePopup() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add new Income");

		ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		ChoiceBox<String> incomeCb = new ChoiceBox<>();
		ChoiceBox<String> concernsCb = new ChoiceBox<>();
		TextField addValue = new TextField();
		addValue.setPromptText("Add amount");
		TextField addComment = new TextField();
		addComment.setPromptText("Add comment");
		CheckBox taxedBox = new CheckBox();
		CheckBox couponBox = new CheckBox();

		incomeItemList = incomeCategoryDao.getList();
		concernsList = incomeConcernsDao.getList();
		for (IncomeCategory incomeItem : incomeItemList) {
			incomeCb.getItems().add(incomeItem.getName());
		}
		for (IncomeConcerns incomeItem : concernsList) {
			concernsCb.getItems().add(incomeItem.getName());
		}

		taxedBox.setSelected(true);

		gridPane.add(new Label("Income: "), 0, 0);
		gridPane.add(incomeCb, 1, 0);
		gridPane.add(new Label("Concerns: "), 0, 1);
		gridPane.add(concernsCb, 1, 1);
		gridPane.add(new Label("Amount: "), 0, 2);
		gridPane.add(addValue, 1, 2);
		gridPane.add(new Label("Comment: "), 0, 3);
		gridPane.add(addComment, 1, 3);
		gridPane.add(new Label("Taxed: "), 0, 4);
		gridPane.add(taxedBox, 1, 4);
		gridPane.add(new Label("Coupon: "), 0, 5);
		gridPane.add(couponBox, 1, 5);

		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE) && addValue.getText() != ""
				&& incomeCb.getValue() != null && incomeCb.getValue() != "" && concernsCb.getValue() != null
				&& concernsCb.getValue() != "") {
			addIncome(addValue.getText(), getMonth(), Integer.valueOf(getYear()), incomeCb.getSelectionModel().getSelectedItem(),
					concernsCb.getSelectionModel().getSelectedItem(), addComment.getText(), taxedBox.isSelected(),
					couponBox.isSelected());
			getIncome(month, year);
		}
	}

	private void editIncomePopup(Income income) {
		IncomeCategory incomeCategory = incomeCategoryDao.findById(income.getCategoryId());
		IncomeConcerns incomeConcerns = incomeConcernsDao.findById(income.getConcernsId());
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Edit Income");

		ButtonType addButton = new ButtonType("Edit", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		ChoiceBox<String> incomeCb = new ChoiceBox<>();
		ChoiceBox<String> concernsCb = new ChoiceBox<>();
		TextField addValue = new TextField();
		addValue.setPromptText("Edit amount");
		TextField addComment = new TextField();
		addValue.setPromptText("Edit comment");
		CheckBox taxedBox = new CheckBox();
		CheckBox couponBox = new CheckBox();

		incomeItemList = incomeCategoryDao.getList();
		concernsList = incomeConcernsDao.getList();
		for (IncomeCategory incomeItem : incomeItemList) {
			incomeCb.getItems().add(incomeItem.getName());
		}
		for (IncomeConcerns incomeItem : concernsList) {
			concernsCb.getItems().add(incomeItem.getName());
		}
		incomeCb.getSelectionModel().select(incomeCategory.getName());
		concernsCb.getSelectionModel().select(incomeConcerns.getName());
		addValue.setText(String.valueOf(income.getValue()));
		addComment.setText(income.getComment());
		if (income.getTaxed() == 0) {
			taxedBox.setSelected(false);
		} else {
			taxedBox.setSelected(true);
		}
		if (income.getCoupon() == 0) {
			couponBox.setSelected(false);
		} else {
			couponBox.setSelected(true);
		}

		gridPane.add(new Label("Income: "), 0, 0);
		gridPane.add(incomeCb, 1, 0);
		gridPane.add(new Label("Concerns: "), 0, 1);
		gridPane.add(concernsCb, 1, 1);
		gridPane.add(new Label("Amount: "), 0, 2);
		gridPane.add(addValue, 1, 2);
		gridPane.add(new Label("Comment: "), 0, 3);
		gridPane.add(addComment, 1, 3);
		gridPane.add(new Label("Taxed: "), 0, 4);
		gridPane.add(taxedBox, 1, 4);
		gridPane.add(new Label("Coupon: "), 0, 5);
		gridPane.add(couponBox, 1, 5);

		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE) && addValue.getText() != ""
				&& incomeCb.getValue() != null && incomeCb.getValue() != "" && concernsCb.getValue() != null
				&& concernsCb.getValue() != "") {
			editIncome(income.getId(), addValue.getText(), income.getDate(),
					incomeCb.getSelectionModel().getSelectedItem(), concernsCb.getSelectionModel().getSelectedItem(),
					addComment.getText(), taxedBox.isSelected(), couponBox.isSelected());
			getIncome(month, year);
		}
	}

	private void addExpensePopup() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add new Expense");

		ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		ChoiceBox<String> categoryCb = new ChoiceBox<>();
		ChoiceBox<String> expenseCb = new ChoiceBox<>();
		TextField addValue = new TextField();
		addValue.setPromptText("Add amount");
		TextField addComment = new TextField();
		addComment.setPromptText("Add comment");
		DatePicker datePicker = new DatePicker();

		initializeCategory(categoryCb);
		initializeExpenseCb(expenseCb);
		initializeDatePicker(datePicker);

		categoryCb.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if (expenseCb.getSelectionModel().getSelectedItem() == null) {
					initializeExpenseCbByCategory(categoryCb.getSelectionModel().getSelectedItem(), expenseCb);
				}

			}

		});
		expenseCb.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				int id = expenseDao.findByName(expenseCb.getSelectionModel().getSelectedItem()).getCategoryId();
				String selected = expenseCategoryDao.findById(id).getName();
				categoryCb.getSelectionModel().select(selected);

			}

		});

		gridPane.add(new Label("Expense: "), 0, 0);
		gridPane.add(expenseCb, 1, 0);
		gridPane.add(new Label("Category: "), 0, 1);
		gridPane.add(categoryCb, 1, 1);
		gridPane.add(new Label("Amount: "), 0, 2);
		gridPane.add(addValue, 1, 2);
		gridPane.add(new Label("Comment: "), 0, 3);
		gridPane.add(addComment, 1, 3);
		gridPane.add(new Label("Date: "), 0, 4);
		gridPane.add(datePicker, 1, 4);

		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE) && addValue.getText() != ""
				&& expenseCb.getValue() != null && expenseCb.getValue() != "" && categoryCb.getValue() != null
				&& categoryCb.getValue() != "" && datePicker.getValue() != null) {
			String amount;
			if (addValue.getText().contains(",")) {
				amount = addValue.getText().replace(",", ".");
			} else {
				amount = addValue.getText();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, datePicker.getValue().getMonthValue() - 1);
			calendar.set(Calendar.YEAR, datePicker.getValue().getYear());
			calendar.set(Calendar.DAY_OF_MONTH, datePicker.getValue().getDayOfMonth());
			Date dateSQL = new Date((calendar.getTime().getTime()));
			addExpense(amount, dateSQL, expenseCb.getSelectionModel().getSelectedItem(),
					categoryCb.getSelectionModel().getSelectedItem(), addComment.getText());
			getExpenses(datePicker.getValue().getMonthValue() - 1,  datePicker.getValue().getYear());

			initializeTotal();
		}
	}

	private void editExpensePopup(Outcome outcome) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Edit Expense");

		ButtonType addButton = new ButtonType("Edit", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		ChoiceBox<String> categoryCb = new ChoiceBox<>();
		ChoiceBox<String> expenseCb = new ChoiceBox<>();
		TextField addValue = new TextField();
		addValue.setPromptText("Edit amount");
		TextField addComment = new TextField();
		addComment.setPromptText("Edit comment");
		DatePicker datePicker = new DatePicker();

		initializeCategory(categoryCb);
		initializeExpenseCb(expenseCb);
		addValue.setText(String.valueOf(outcome.getValue()));
		addComment.setText(outcome.getComment());
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			datePicker
					.setValue(format.parse(outcome.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		} catch (ParseException e) {
			Stacktrace.print(e);
		}

		categoryCb.getSelectionModel().select(expenseCategoryDao.findById(outcome.getCategoryId()).getName());
		expenseCb.getSelectionModel().select(expenseDao.findById(outcome.getExpenseId()).getName());

		categoryCb.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				initializeExpenseCbByCategory(categoryCb.getSelectionModel().getSelectedItem(), expenseCb);
			}

		});

		gridPane.add(new Label("Expense: "), 0, 0);
		gridPane.add(expenseCb, 1, 0);
		gridPane.add(new Label("Category: "), 0, 1);
		gridPane.add(categoryCb, 1, 1);
		gridPane.add(new Label("Amount: "), 0, 2);
		gridPane.add(addValue, 1, 2);
		gridPane.add(new Label("Comment: "), 0, 3);
		gridPane.add(addComment, 1, 3);
		gridPane.add(new Label("Date: "), 0, 4);
		gridPane.add(datePicker, 1, 4);

		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE) && addValue.getText() != ""
				&& expenseCb.getValue() != null && expenseCb.getValue() != "" && categoryCb.getValue() != null
				&& categoryCb.getValue() != "" && datePicker.getValue() != null) {
			String amount;
			if (addValue.getText().contains(",")) {
				amount = addValue.getText().replace(",", ".");
			} else {
				amount = addValue.getText();
			}

			Date date = Date.valueOf(datePicker.getValue());

			editExpense(outcome.getId(), expenseDao.findByName(expenseCb.getSelectionModel().getSelectedItem()).getId(),
					expenseCategoryDao.findByName(categoryCb.getSelectionModel().getSelectedItem()).getId(), amount,
					date, addComment.getText());
			getExpenses(month, year);

			initializeTotal();
		}
	}

	public void initializeTotal() {
		double expensesTotal = 0.0;
		double incomeTotal = 0.0;
		double monthTotal = 0.0;
		expensesTotal = outcomeDao.getTotalByMonthAndYear(month, year);
		incomeTotal = incomeDao.getTotalByMonthAndYear(month, year);
		monthTotal = incomeTotal - expensesTotal;
		double expensesTotalRound = Math.round(expensesTotal * 100.0) / 100.0;
		double incomeTotalRound = Math.round(incomeTotal * 100.0) / 100.0;
		double monthTotalRound = Math.round(monthTotal * 100.0) / 100.0;
		expenseLb.setText(String.valueOf(expensesTotalRound) + " " + currency);
		incomeLb.setText(String.valueOf(incomeTotalRound) + " " + currency);
		totalLb.setText(String.valueOf(monthTotalRound) + " " + currency);
		if (monthTotal > 0) {
			totalLb.setTextFill(Color.GREEN);
		} else if (monthTotal < 0) {
			totalLb.setTextFill(Color.RED);
		} else {
			totalLb.setTextFill(Color.YELLOW);
		}
		getIncome(month, year);
		getExpenses(month, year);
	}

	private void initializeExpensePane() {
		expenseTableSmall.layoutXProperty()
				.bind(expensePane.widthProperty().subtract(expenseTableSmall.widthProperty()).divide(2));
		expenseTableSmall.prefHeightProperty().bind(expensePane.heightProperty().subtract(80.0));
		expenseTitleLb.layoutXProperty()
				.bind(expensePane.widthProperty().subtract(expenseTitleLb.widthProperty()).divide(2));
		submitBtn.layoutXProperty()
				.bind(expensePane.widthProperty().subtract(expenseTitleLb.widthProperty()).divide(2).subtract(10.0));
		submitBtn.layoutYProperty().bind(expensePane.heightProperty().subtract(30.0));
		deleteBtn.layoutXProperty()
				.bind(expensePane.widthProperty().add(expenseTitleLb.widthProperty()).divide(2).add(10.0));
		deleteBtn.layoutYProperty().bind(expensePane.heightProperty().subtract(30.0));
		editExpenseBtn.layoutXProperty()
				.bind(expensePane.widthProperty().subtract(expenseTitleLb.widthProperty()).divide(2).add(57.0));
		editExpenseBtn.layoutYProperty().bind(expensePane.heightProperty().subtract(30.0));
	}

	private void initializeIncomePane() {
		incomeTable.layoutXProperty().bind(incomePane.widthProperty().subtract(incomeTable.widthProperty()).divide(2));
		incomeTable.prefHeightProperty().bind(incomePane.heightProperty().subtract(100.0));
		incomeTitleLb.layoutXProperty()
				.bind(incomePane.widthProperty().subtract(incomeTitleLb.widthProperty()).divide(2));
		submitIncomeBtn.layoutXProperty()
				.bind(incomePane.widthProperty().subtract(incomeTitleLb.widthProperty()).divide(2).subtract(10.0));
		submitIncomeBtn.layoutYProperty().bind(incomePane.heightProperty().subtract(30.0));
		deleteIncomeBtn.layoutXProperty()
				.bind(incomePane.widthProperty().add(incomeTitleLb.widthProperty()).divide(2).add(10.0));
		deleteIncomeBtn.layoutYProperty().bind(incomePane.heightProperty().subtract(30.0));
		editIncomeBtn.layoutXProperty()
				.bind(incomePane.widthProperty().subtract(incomeTitleLb.widthProperty()).divide(2).add(57.0));
		editIncomeBtn.layoutYProperty().bind(incomePane.heightProperty().subtract(30.0));
	}

	private void initializePanes() {
		initializeExpensePane();
		initializeIncomePane();
	}
	
	private void initializeCurMonth() {
//		monthDropdown.getItems().clear();
//		monthDropdown.getItems().add("January");
//		monthDropdown.getItems().add("February");
//		monthDropdown.getItems().add("March");
//		monthDropdown.getItems().add("April");
//		monthDropdown.getItems().add("May");
//		monthDropdown.getItems().add("June");
//		monthDropdown.getItems().add("July");
//		monthDropdown.getItems().add("August");
//		monthDropdown.getItems().add("September");
//		monthDropdown.getItems().add("October");
//		monthDropdown.getItems().add("November");
//		monthDropdown.getItems().add("December");
//		monthDropdown.setValue(DateUtil.getMonth());
//		yearDropdown.getItems().clear();
//		yearDropdown.getItems().add(String.valueOf(DateUtil.getYear() -1));
//		yearDropdown.getItems().add(String.valueOf(DateUtil.getYear() -2));
//		yearDropdown.getItems().add(String.valueOf(DateUtil.getYear()));
//		yearDropdown.getItems().add(String.valueOf(DateUtil.getYear() +1));
//		yearDropdown.getItems().add(String.valueOf(DateUtil.getYear() +2));
//		yearDropdown.setValue(String.valueOf(DateUtil.getYear()));
		curMonth.setText(DateUtil.getMonth());
		curYear.setText(String.valueOf(DateUtil.getYear()));
	}

	public void changeDate(int month, int year){
		curMonth.setText(DateUtil.getMonthForInt(month));
		curYear.setText(String.valueOf(year));
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
