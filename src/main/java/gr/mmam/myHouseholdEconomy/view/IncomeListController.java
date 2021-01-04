package gr.mmam.myHouseholdEconomy.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gr.mmam.myHouseholdEconomy.MainApp;
import gr.mmam.myHouseholdEconomy.dao.IncomeCategoryDAO;
import gr.mmam.myHouseholdEconomy.dao.IncomeConcernsDao;
import gr.mmam.myHouseholdEconomy.model.IncomeCategory;
import gr.mmam.myHouseholdEconomy.model.IncomeConcerns;
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

public class IncomeListController {

	IncomeCategoryDAO incomeCategoryDao = new IncomeCategoryDAO();
	IncomeConcernsDao incomeConcernsDao = new IncomeConcernsDao();

	@FXML
	Pane incomePane = new Pane();

	@FXML
	Pane concernsPane = new Pane();

	@FXML
	Label incomeLabel = new Label();

	@FXML
	Label concernsLabel = new Label();

	@FXML
	private TableView<IncomeCategory> incomeCategoryTable;

	@FXML
	private TableView<IncomeConcerns> incomeConcernsTable;

	@FXML
	private TableColumn<IncomeCategory, String> colCatName;

	@FXML
	private TableColumn<IncomeConcerns, String> colConName;

	List<IncomeCategory> incomeCatList = new ArrayList<IncomeCategory>();
	List<IncomeConcerns> incomeConList = new ArrayList<IncomeConcerns>();

	ObservableList<String> observableList = FXCollections.observableArrayList();

	@FXML
	Button addCatBtn = new Button();

	@FXML
	Button addConBtn = new Button();

	@FXML
	Button editCatBtn = new Button();

	@FXML
	Button editConBtn = new Button();

	@FXML
	Button deleteCatBtn = new Button();

	@FXML
	Button deleteConBtn = new Button();

	@SuppressWarnings("unused")
	private MainApp mainApp;
	private ObservableList<IncomeCategory> dataCat;
	private ObservableList<IncomeConcerns> dataCon;

	public IncomeListController() {

	}

	@FXML
	private void initialize() {
		getIncome();
		initializeConcerns();
		deleteCatBtn.setDisable(true);
		deleteConBtn.setDisable(true);
		editConBtn.setDisable(true);
		initializePanes();
		incomeCategoryTable.setRowFactory(tv -> {
			TableRow<IncomeCategory> row = new TableRow<IncomeCategory>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					editCatBtn.setDisable(false);
					deleteCatBtn.setDisable(false);
				} else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
					editIncomePopup(incomeCategoryTable.getSelectionModel().getSelectedItem());
					incomeCategoryTable.getSelectionModel().clearSelection();
					editCatBtn.setDisable(true);
					deleteCatBtn.setDisable(true);
				} else {
					editCatBtn.setDisable(true);
					deleteCatBtn.setDisable(true);
				}
			});
			return row;
		});
		incomeConcernsTable.setRowFactory(tv -> {
			TableRow<IncomeConcerns> row = new TableRow<IncomeConcerns>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					editConBtn.setDisable(false);
					deleteConBtn.setDisable(false);
				} else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
					editConcernsPopup(incomeConcernsTable.getSelectionModel().getSelectedItem());
					incomeConcernsTable.getSelectionModel().clearSelection();
					editConBtn.setDisable(true);
					deleteConBtn.setDisable(true);
				} else {
					editConBtn.setDisable(true);
					deleteConBtn.setDisable(true);
				}
			});
			return row;
		});
		addCatBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addIncomePopup();
			}
		});
		editCatBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				editIncomePopup(incomeCategoryTable.getSelectionModel().getSelectedItem());
			}
		});
		addConBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addConcernsPopup();
			}
		});
		editConBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				editConcernsPopup(incomeConcernsTable.getSelectionModel().getSelectedItem());
			}
		});
		deleteConBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Alert wait = new Alert(Alert.AlertType.INFORMATION, "Deleting concerns Type...");
				wait.setTitle(null);
				wait.setHeaderText(null);
				if (incomeConcernsTable.getSelectionModel().getSelectedIndex() != -1) {
					Alert alert = new Alert(AlertType.CONFIRMATION,
							"Are you sure you want to delete Concerns \""
									+ incomeConcernsTable.getSelectionModel().getSelectedItem().getName() + "\" ?",
							ButtonType.YES, ButtonType.CANCEL);
					alert.setHeaderText(null);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.YES) {
						wait.show();
						removeFromConcerns(
								incomeConcernsTable.getSelectionModel().getSelectedItem().getId());

						initializeConcerns();

						wait.setResult(ButtonType.OK);
						wait.close();
					}
				} else {
					Alert alert = new Alert(AlertType.WARNING, "Select an item from the list in order to delete it");
					alert.setHeaderText(null);
					alert.showAndWait();
				}
			}
		});
		deleteCatBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Alert wait = new Alert(Alert.AlertType.INFORMATION, "Deleting income Type...");
				wait.setTitle(null);
				wait.setHeaderText(null);
				if (incomeCategoryTable.getSelectionModel().getSelectedIndex() != -1) {
					Alert alert = new Alert(AlertType.CONFIRMATION,
							"Are you sure you want to delete Expense \""
									+ incomeCategoryTable.getSelectionModel().getSelectedItem().getName() + "\" ?",
							ButtonType.YES, ButtonType.CANCEL);
					alert.setHeaderText(null);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.YES) {
						wait.show();
						removeFromIncome(
								incomeCategoryTable.getSelectionModel().getSelectedItem().getId());

						getIncome();

						wait.setResult(ButtonType.OK);
						wait.close();
					}
				} else {
					Alert alert = new Alert(AlertType.WARNING, "Select an item from the list in order to delete it");
					alert.setHeaderText(null);
					alert.showAndWait();
				}
			}
		});
	}

	public void getIncome() {
		incomeCatList.clear();
		dataCat = FXCollections.observableArrayList();
		incomeCatList = incomeCategoryDao.getList();
		dataCat.addAll(incomeCatList);
		colCatName.setCellValueFactory(new PropertyValueFactory<IncomeCategory, String>("name"));
		incomeCategoryTable.setItems(null);
		incomeCategoryTable.setItems(dataCat);
		incomeCategoryTable.refresh();
	}

	private void initializeConcerns() {
		incomeConList.clear();
		dataCon = FXCollections.observableArrayList();
		incomeConList = incomeConcernsDao.getList();
		dataCon.addAll(incomeConList);
		colConName.setCellValueFactory(new PropertyValueFactory<IncomeConcerns, String>("name"));
		incomeConcernsTable.setItems(null);
		incomeConcernsTable.setItems(dataCon);
		incomeConcernsTable.refresh();
	}

	private void addConcerns(String name) {
		incomeConcernsDao.save(new IncomeConcerns(name));
		initializeConcerns();
	}

	private void editConcerns(IncomeConcerns incomeConcerns) {
		incomeConcernsDao.update(incomeConcerns);
		initializeConcerns();
	}

	public void removeFromConcerns(int id) {
		incomeConcernsDao.delete(incomeConcernsDao.findById(id));
		initializeConcerns();
	}

	public void addIncome(TextField name) {
		incomeCategoryDao.save(new IncomeCategory(name.getText()));
		getIncome();
	}
	public void editIncome(IncomeCategory incomeCategory) {
		incomeCategoryDao.update(incomeCategory);
		getIncome();
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

		TextField addIncomeInput = new TextField();
		addIncomeInput.setPromptText("Add Name");

		gridPane.add(new Label("Name: "), 0, 0);
		gridPane.add(addIncomeInput, 1, 0);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			addIncome(addIncomeInput);
		}
	}
	
	private void editIncomePopup(IncomeCategory incomeCategory) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Edit Income");

		ButtonType addButton = new ButtonType("Update", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		TextField addIncomeInput = new TextField();
		addIncomeInput.setText(incomeCategory.getName());

		gridPane.add(new Label("Name: "), 0, 0);
		gridPane.add(addIncomeInput, 1, 0);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			incomeCategory.setName(addIncomeInput.getText());
			editIncome(incomeCategory);
			editCatBtn.setDisable(true);
			deleteCatBtn.setDisable(true);
		}
	}

	private void addConcernsPopup() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add new Concern");

		ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		TextField addConcernInput = new TextField();
		addConcernInput.setPromptText("Add Name");

		gridPane.add(new Label("Name: "), 0, 0);
		gridPane.add(addConcernInput, 1, 0);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			addConcerns(addConcernInput.getText());
		}
	}

	private void editConcernsPopup(IncomeConcerns incomeConcerns) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add new Concern");

		ButtonType addButton = new ButtonType("Update", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		TextField addConcernInput = new TextField();
		addConcernInput.setText(incomeConcerns.getName());

		gridPane.add(new Label("Name: "), 0, 0);
		gridPane.add(addConcernInput, 1, 0);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			incomeConcerns.setName(addConcernInput.getText());
			editConcerns(incomeConcerns);
			editConBtn.setDisable(true);
			deleteConBtn.setDisable(true);
		}
	}

	private void initializeIncomePane() {
		incomeCategoryTable.layoutXProperty()
				.bind(incomePane.widthProperty().subtract(incomeCategoryTable.widthProperty()).divide(2));
		incomeLabel.layoutXProperty().bind(incomePane.widthProperty().subtract(incomeLabel.widthProperty()).divide(2));
		addCatBtn.layoutXProperty().bind(incomePane.widthProperty().subtract(incomeLabel.widthProperty()).divide(2));
		deleteCatBtn.layoutXProperty().bind(incomePane.widthProperty().add(incomeLabel.widthProperty()).divide(2));
		editCatBtn.layoutXProperty().bind(incomePane.widthProperty().subtract(editCatBtn.widthProperty().divide(2)).divide(2));
	}

	private void initializeConcernsPane() {
		incomeConcernsTable.layoutXProperty()
				.bind(concernsPane.widthProperty().subtract(incomeConcernsTable.widthProperty()).divide(2));
		concernsLabel.layoutXProperty()
				.bind(concernsPane.widthProperty().subtract(concernsLabel.widthProperty()).divide(2));
		addConBtn.layoutXProperty()
				.bind(concernsPane.widthProperty().subtract(concernsLabel.widthProperty()).divide(2));
		deleteConBtn.layoutXProperty().bind(concernsPane.widthProperty().add(concernsLabel.widthProperty()).divide(2));
		editConBtn.layoutXProperty().bind(concernsPane.widthProperty().subtract(editConBtn.widthProperty().divide(2)).divide(2));
	}

	private void initializePanes() {
		initializeIncomePane();
		initializeConcernsPane();
	}

	public void removeFromIncome(int id) {
		incomeCategoryDao.delete(incomeCategoryDao.findById(id));
		getIncome();
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
