package gr.mmam.myHouseholdEconomy.view;

import java.io.File;

import gr.mmam.myHouseholdEconomy.MainApp;
import gr.mmam.myHouseholdEconomy.dao.OptionsDao;
import gr.mmam.myHouseholdEconomy.util.ExcelReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OptionsController {

	MainController mainController = new MainController();

	OptionsDao optionsDao = new OptionsDao();
	
	@FXML
	Label currencyLabel = new Label();
	
	@FXML
	ChoiceBox<String> currency = new ChoiceBox<>();
	
	@FXML
	Button changeCurrencyBtn = new Button();

	@FXML
	Button clearOutcomeBtn = new Button();
	
	@FXML
	Button clearExpensesBtn = new Button();
	
	@FXML
	Button clearCategoriesBtn = new Button();
	
	@FXML
	Button browseExcelBtn = new Button();
	
	@FXML
	Button loadExcelBtn = new Button();
	
	@FXML
	TextField browseExcelField = new TextField();
	
	FileChooser fileChooser = new FileChooser();

	@SuppressWarnings("unused")
	private MainApp mainApp;
	
	private Stage stage;

	public OptionsController() {

	}

	@FXML
	private void initialize() {
		initializeCurrency();
		loadExcelBtn.setDisable(true);
		changeCurrencyBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (currency.getSelectionModel().getSelectedItem()!= null && currency.getSelectionModel().getSelectedItem()!= "") {
					setCurrency(currency.getSelectionModel().getSelectedItem());
				}
			}
		});
		clearOutcomeBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Are you se you want to delete ALL expenses ?",
						ButtonType.YES, ButtonType.CANCEL);
				alert.setHeaderText(null);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					optionsDao.clearOutcomes();
				}
			}
		});
		clearExpensesBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Are you se you want to delete ALL expense Sub Categories ?",
						ButtonType.YES, ButtonType.CANCEL);
				alert.setHeaderText(null);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					optionsDao.clearExpenses();
				}
			}
		});
		clearCategoriesBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Are you se you want to delete ALL expense Categories ?",
						ButtonType.YES, ButtonType.CANCEL);
				alert.setHeaderText(null);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					optionsDao.clearExpenseCategories();
				}
			}
		});
		
		browseExcelBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
                fileChooser.getExtensionFilters().add(extFilter);
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					browseExcelField.setText(file.getAbsolutePath());
					loadExcelBtn.setDisable(false);
                }
			}
		});
		loadExcelBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				ExcelReader.read(browseExcelField.getText());
			}
		});
	}
	
	private void initializeCurrency() {
		currency.getItems().clear();
		currency.getItems().add("€");
		currency.getItems().add("$");
		currency.getSelectionModel().select(optionsDao.getCurrency());
	}
	
	private void setCurrency(String currency) {
		optionsDao.setCurrency(currency);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
