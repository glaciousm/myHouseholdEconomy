package gr.mmam.myHouseholdEconomy.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gr.mmam.myHouseholdEconomy.MainApp;
import gr.mmam.myHouseholdEconomy.dao.ExpenseCategoryDAO;
import gr.mmam.myHouseholdEconomy.dao.ExpenseDAO;
import gr.mmam.myHouseholdEconomy.dao.OutcomeDao;
import gr.mmam.myHouseholdEconomy.model.Expense;
import gr.mmam.myHouseholdEconomy.model.ExpenseCategory;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class CategoriesListController {

	ExpenseCategoryDAO expenseCategoryDao = new ExpenseCategoryDAO();
	ExpenseDAO expenseDao = new ExpenseDAO();
	OutcomeDao outcomeDao = new OutcomeDao();

	@FXML
	Pane categoryListPane = new Pane();

	@FXML
	Pane expenseListPane = new Pane();

	@FXML
	Label categoryListLabel = new Label();

	@FXML
	Label expenseListLabel = new Label();

	@FXML
	private TableView<ExpenseCategory> categoriesTable;

	@FXML
	private TableView<Expense> subCategoriesTable;

	@FXML
	private TableColumn<ExpenseCategory, String> colName;

	@FXML
	private TableColumn<Expense, String> colSubName;

	ObservableList<String> observableList = FXCollections.observableArrayList();

	@FXML
	Button addBtn = new Button();

	@FXML
	Button editBtn = new Button();

	@FXML
	Button deleteBtn = new Button();

	@FXML
	Button addSubBtn = new Button();

	@FXML
	Button editSubBtn = new Button();

	@FXML
	Button deleteSubBtn = new Button();

	@SuppressWarnings("unused")
	private MainApp mainApp;

	private ObservableList<ExpenseCategory> data;

	private ObservableList<Expense> dataSub;

	public CategoriesListController() {

	}

	@FXML
	private void initialize() {
		initializePanes();
		getCategories();
		deleteBtn.setDisable(true);
		addSubBtn.setDisable(true);
		deleteSubBtn.setDisable(true);
		categoriesTable.setRowFactory(tv -> {
			TableRow<ExpenseCategory> row = new TableRow<ExpenseCategory>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					ExpenseCategory clickedRow = row.getItem();
					deleteBtn.setDisable(false);
					addSubBtn.setDisable(false);
					getSubCategories(clickedRow.getId());
				} else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
					editCategoryPopup(categoriesTable.getSelectionModel().getSelectedItem());
				}
			});
			return row;
		});
		subCategoriesTable.setRowFactory(tv -> {
			TableRow<Expense> row = new TableRow<Expense>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					deleteSubBtn.setDisable(false);
					editSubBtn.setDisable(false);
				} else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
					editSubCategoryPopup(subCategoriesTable.getSelectionModel().getSelectedItem());
				}
			});
			return row;
		});
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addCategoryPopup();
			}
		});
		editBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				editCategoryPopup(categoriesTable.getSelectionModel().getSelectedItem());
			}
		});
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Are you sure you want to delete Category \""
								+ categoriesTable.getSelectionModel().getSelectedItem().getName() + "\" ?",
						ButtonType.YES, ButtonType.CANCEL);
				alert.setHeaderText(null);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					removeFromCategories(categoriesTable.getSelectionModel().getSelectedItem().getId());
				}
			}
		});
		addSubBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addSubCategoryPopup(categoriesTable.getSelectionModel().getSelectedItem());
			}
		});
		editSubBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				editSubCategoryPopup(subCategoriesTable.getSelectionModel().getSelectedItem());
				subCategoriesTable.refresh();
			}
		});
		deleteSubBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Are you sure you want to delete Sub Category \""
								+ subCategoriesTable.getSelectionModel().getSelectedItem().getName() + "\" ?",
						ButtonType.YES, ButtonType.CANCEL);
				alert.setHeaderText(null);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					int catId = subCategoriesTable.getSelectionModel().getSelectedItem().getCategoryId();
					removeFromSubCategories(subCategoriesTable.getSelectionModel().getSelectedItem().getId());
					getSubCategories(catId);
				}
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getCategories() {
		List<ExpenseCategory> list = new ArrayList();
		list = expenseCategoryDao.getList();
		data = FXCollections.observableArrayList();
		if (list != null) {
			data.addAll(list);
			colName.setCellValueFactory(new PropertyValueFactory<ExpenseCategory, String>("name"));

			categoriesTable.setItems(null);
			categoriesTable.setItems(data);
			categoriesTable.refresh();
		} else {
			categoriesTable.setItems(null);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubCategories(int id) {
		List<Expense> list = new ArrayList();
		list = expenseDao.findByCategoryId(id);
		dataSub = FXCollections.observableArrayList();
		if (list != null) {
			dataSub.addAll(list);
			colSubName.setCellValueFactory(new PropertyValueFactory<Expense, String>("name"));

			subCategoriesTable.setItems(null);
			subCategoriesTable.setItems(dataSub);
			subCategoriesTable.refresh();

		} else {
			subCategoriesTable.setItems(null);
		}
	}

	public void addCategory(TextField name) {
		ExpenseCategory expenseCategory = new ExpenseCategory(name.getText());
		expenseCategoryDao.save(expenseCategory);
		getCategories();
	}

	public void editCategory(ExpenseCategory expenseCategory) {
		expenseCategoryDao.update(expenseCategory);
		getCategories();
	}

	public void removeFromCategories(int id) {
		ExpenseCategory expenseCategory = expenseCategoryDao.findById(id);
		expenseCategoryDao.delete(expenseCategory);
		getCategories();
	}

	public void addSubCategory(TextField name, ExpenseCategory expenseCategory) {
		Expense expense = new Expense(name.getText(), expenseCategory.getId());
		expenseDao.save(expense);
		getSubCategories(expenseCategory.getId());
	}

	public void editSubCategory(Expense expense) {
		expenseDao.update(expense);
		outcomeDao.changeCategory(expense);
		ExpenseCategory cat = expenseCategoryDao.findById(expense.getCategoryId());
		getSubCategories(cat.getId());
		for (int i = 0; i < categoriesTable.getItems().size(); i++) {
			if (expense.getCategoryId() == categoriesTable.getItems().get(i).getId()) {
				categoriesTable.getSelectionModel().select(i);
				break;
			}
		}
	}

	public void removeFromSubCategories(int id) {
		Expense expense = expenseDao.findById(id);
		expenseDao.delete(expense);
		getSubCategories(id);
	}

	private void initializeCategoriesListPane() {
		categoriesTable.layoutXProperty()
				.bind(categoryListPane.widthProperty().subtract(categoriesTable.widthProperty()).divide(2));
		categoryListLabel.layoutXProperty()
				.bind(categoryListPane.widthProperty().subtract(categoryListLabel.widthProperty()).divide(2));
		addBtn.layoutXProperty()
				.bind(categoryListPane.widthProperty().subtract(categoryListLabel.widthProperty()).divide(2));
		deleteBtn.layoutXProperty()
				.bind(categoryListPane.widthProperty().add(categoryListLabel.widthProperty()).divide(2));
		editBtn.layoutXProperty()
				.bind(categoryListPane.widthProperty().subtract(editBtn.widthProperty().divide(2)).divide(2));
	}

	private void initializeExpensePane() {
		subCategoriesTable.layoutXProperty()
				.bind(expenseListPane.widthProperty().subtract(subCategoriesTable.widthProperty()).divide(2));
		expenseListLabel.layoutXProperty()
				.bind(expenseListPane.widthProperty().subtract(expenseListLabel.widthProperty()).divide(2));
		addSubBtn.layoutXProperty()
				.bind(expenseListPane.widthProperty().subtract(expenseListLabel.widthProperty()).divide(2));
		deleteSubBtn.layoutXProperty()
				.bind(expenseListPane.widthProperty().add(expenseListLabel.widthProperty()).divide(2));
		editSubBtn.layoutXProperty()
				.bind(expenseListPane.widthProperty().subtract(editSubBtn.widthProperty().divide(2)).divide(2));
	}

	private void initializePanes() {
		initializeCategoriesListPane();
		initializeExpensePane();
	}

	private void addCategoryPopup() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add new Category");

		ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		TextField addCategoryInput = new TextField();
		addCategoryInput.setPromptText("Add category");

		gridPane.add(new Label("Category: "), 0, 0);
		gridPane.add(addCategoryInput, 1, 0);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			addCategory(addCategoryInput);
		}
	}

	private void editCategoryPopup(ExpenseCategory expenseCategory) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Edit Category");

		ButtonType addButton = new ButtonType("Update", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		TextField addCategoryInput = new TextField();
		addCategoryInput.setText(expenseCategory.getName());

		gridPane.add(new Label("Category: "), 0, 0);
		gridPane.add(addCategoryInput, 1, 0);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			expenseCategory.setName(addCategoryInput.getText());
			editCategory(expenseCategory);
		}
	}

	private void addSubCategoryPopup(ExpenseCategory expenseCategory) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add new Sub Category");

		ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		TextField addSubCategoryInput = new TextField();
		addSubCategoryInput.setPromptText("Add sub category");

		gridPane.add(new Label("Category: "), 0, 0);
		gridPane.add(addSubCategoryInput, 1, 0);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			addSubCategory(addSubCategoryInput, expenseCategory);
		}
	}

	private void editSubCategoryPopup(Expense expense) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Edit Sub Category");

		ButtonType addButton = new ButtonType("Update", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		TextField addSubCategoryInput = new TextField();
		addSubCategoryInput.setText(expense.getName());
		ChoiceBox<String> categoryCb = new ChoiceBox<>();
		ObservableList<String> observableListCat = FXCollections.observableArrayList();
		for (ExpenseCategory cat : expenseCategoryDao.getList()) {
			observableListCat.add(cat.getName());
		}
		categoryCb.setItems(observableListCat);
		categoryCb.getSelectionModel().select(expenseCategoryDao.findById(expense.getCategoryId()).getName());
		addSubCategoryInput.setText(expense.getName());

		gridPane.add(new Label("Expense: "), 0, 0);
		gridPane.add(addSubCategoryInput, 1, 0);
		gridPane.add(new Label("Category: "), 0, 1);
		gridPane.add(categoryCb, 1, 1);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			expense.setName(addSubCategoryInput.getText());
			expense.setCategoryId(expenseCategoryDao.findByName(categoryCb.getValue()).getId());
			editSubCategory(expense);
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
