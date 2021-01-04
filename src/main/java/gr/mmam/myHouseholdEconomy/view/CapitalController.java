package gr.mmam.myHouseholdEconomy.view;

import java.sql.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gr.mmam.myHouseholdEconomy.MainApp;
import gr.mmam.myHouseholdEconomy.dao.CapitalDao;
import gr.mmam.myHouseholdEconomy.model.Capital;
import gr.mmam.myHouseholdEconomy.util.DateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class CapitalController {
	
	MainController mainController = new MainController();
	
	CapitalDao capitalDao = new CapitalDao();
	
	@FXML
	Button addBtn = new Button();
	
	@FXML
	Label title = new Label();
	
	@FXML
	TableView<Capital> table = new TableView<>();
	
	@FXML
	TableColumn<Capital, String> balanceCol = new TableColumn<>();
	
	@FXML
	TableColumn<Capital, String> dateCol = new TableColumn<>();
	
	@FXML
	TableColumn<Capital, String> commentCol = new TableColumn<>();
	
	private ObservableList<Capital> data;
	private List<Capital> list = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private MainApp mainApp;

	public CapitalController() {

	}

	@FXML
	private void initialize() {
		getCheckpoints();
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addCapitalPopup();
				getCheckpoints();
			}
		});
	}
	
	private void getCheckpoints() {
		list.clear();
		data = FXCollections.observableArrayList();
		list = capitalDao.getList();
			for (Capital capital : list) {
				data.add(capital);
			}
			balanceCol.setCellValueFactory(new PropertyValueFactory<Capital, String>("value"));
			commentCol.setCellValueFactory(new PropertyValueFactory<Capital, String>("comment"));
			dateCol.setCellValueFactory(new PropertyValueFactory<Capital, String>("date"));
			table.setItems(null);
			table.setItems(data);
	}
	
	private void addCapitalPopup() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add new Category");

		ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		TextField addValueInput = new TextField();
		addValueInput.setPromptText("Add Value");
		TextField addCommentInput = new TextField();
		addCommentInput.setPromptText("Add Comment");

		gridPane.add(new Label("Value: "), 0, 0);
		gridPane.add(addValueInput, 1, 0);
		gridPane.add(new Label("Comment: "), 0, 1);
		gridPane.add(addCommentInput, 1, 1);
		dialog.getDialogPane().setContent(gridPane);
		
		Optional<ButtonType> result = dialog.showAndWait();
		
		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			addCapital(addValueInput.getText(), addCommentInput.getText());
		}
	}
	
	private void addCapital(String value, String comment) {
		OverviewController overviewController = new OverviewController();
		java.util.Date utilStartDate = Date.from(DateUtil.getToday().atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
		capitalDao.save(new Capital(Double.parseDouble(value), comment, sqlStartDate, overviewController.initializeYearTotal(DateUtil.getYear())));
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
