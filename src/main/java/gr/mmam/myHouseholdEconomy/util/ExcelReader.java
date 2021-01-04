package gr.mmam.myHouseholdEconomy.util;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import gr.mmam.myHouseholdEconomy.dao.ExpenseCategoryDAO;
import gr.mmam.myHouseholdEconomy.dao.ExpenseDAO;
import gr.mmam.myHouseholdEconomy.dao.OutcomeDao;
import gr.mmam.myHouseholdEconomy.model.Expense;
import gr.mmam.myHouseholdEconomy.model.ExpenseCategory;
import gr.mmam.myHouseholdEconomy.model.Outcome;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ExcelReader {

	static List<String> nameList = new ArrayList<>();
	static List<Date> dateList = new ArrayList<>();
	static List<Double> amountList = new ArrayList<>();
	static List<String> commentList = new ArrayList<>();
	static List<String> tempNameList = new ArrayList<>();
	static List<Date> tempDateList = new ArrayList<>();
	static List<Double> tempAmountList = new ArrayList<>();
	static List<String> tempCommentList = new ArrayList<>();
	static List<String> tempExpenseList = new ArrayList<>();
	static ExpenseCategoryDAO expenseCategoryDAO = new ExpenseCategoryDAO();
	static ExpenseDAO expenseDAO = new ExpenseDAO();
	static OutcomeDao outcomeDao = new OutcomeDao();
	static String tempName = "";

	public static void read(String path) {
		nameList.clear();
		dateList.clear();
		amountList.clear();
		commentList.clear();

		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(new File(path));
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			Stacktrace.print(e);
		}

		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		for (int k = 0; k < workbook.getNumberOfSheets(); k++) {
			Sheet sheet = workbook.getSheetAt(k);
			System.out.println("=> " + sheet.getSheetName() + " with rows: " + sheet.getPhysicalNumberOfRows());
			for (int j = 0; j < sheet.getPhysicalNumberOfRows() - 1; j++) {
				Row row = sheet.getRow(j + 1);
				for (int i = 0; i < 4; i++) {
					if (i == 0 && row.getCell(i) != null && !row.getCell(i).equals("")) {
						String name = row.getCell(i).getStringCellValue();
						name = name.replace("'", "");
						nameList.add(name);

					} else if (i == 1 && !row.getCell(i).equals("")) {
						java.sql.Date sql = new java.sql.Date(row.getCell(i).getDateCellValue().getTime());
						dateList.add(sql);
					} else if (i == 2 && !row.getCell(i).equals("")) {
						String strNum = row.getCell(i).toString().replace(",", ".");
						if (row.getCell(i).toString().contains("+") || row.getCell(i).toString().contains("*")
								|| row.getCell(i).toString().contains("/")) {
							Alert alert = new Alert(AlertType.CONFIRMATION,
									"Load failed because this value found in cell " + i + " in tab "
											+ sheet.getSheetName() + " : " + row.getCell(i).toString(),
									ButtonType.YES, ButtonType.CANCEL);
							alert.setHeaderText(null);
							alert.showAndWait();
						}
						Double num = Double.valueOf(strNum);
						amountList.add(num);
					} else if (i == 3) {
						if (row.getCell(i) == null) {
							commentList.add("");
						} else {
							commentList.add(row.getCell(i).getStringCellValue());
						}
					}
				}
			}
		}
		validateDataFromLists();

		Alert alert = new Alert(AlertType.CONFIRMATION, "Loading Completed Succesfully!", ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
		try {
			workbook.close();
		} catch (IOException e) {
			Stacktrace.print(e);
		}
	}

	private static void validateDataFromLists() {
		tempNameList.clear();
		tempDateList.clear();
		tempAmountList.clear();
		tempCommentList.clear();
		tempExpenseList.clear();
		for (int i = 0; i < nameList.size(); i++) {
			if (expenseDAO.findByName(nameList.get(i)) == null) {
				tempNameList.add(nameList.get(i));
				tempDateList.add(dateList.get(i));
				tempAmountList.add(amountList.get(i));
				tempCommentList.add(commentList.get(i));
			} else {
				Expense expense = expenseDAO.findByName(nameList.get(i));
				outcomeDao.save(new Outcome(expense.getId(), expense.getCategoryId(), amountList.get(i), dateList.get(i), commentList.get(i)));
			}
		}
		if (tempNameList.size() != 0) {
			for (int i = 0; i < tempNameList.size(); i++) {
				if (expenseDAO.findByName(tempNameList.get(i)) == null) {
						Alert alert = new Alert(AlertType.CONFIRMATION,
								"Loading found expense sub category " + tempNameList.get(i)
										+ " that is not in the database, do you want to add it now? \t"
										+ "If not, it will be added to category 'Temporary' so you can add it later",
								ButtonType.YES, ButtonType.NO);
						alert.setHeaderText(null);
						alert.showAndWait();
						if (alert.getResult() == ButtonType.YES) {

							expenseFromExcelUpdateAndSave(tempNameList.get(i), tempAmountList.get(i),
									tempDateList.get(i), tempCommentList.get(i));

						} else {
							if (expenseCategoryDAO.findByName("Temporary") != null) {
								expenseDAO.save(
										new Expense(tempNameList.get(i), expenseCategoryDAO.findByName("Temporary").getId()));
								outcomeDao.save(new Outcome(expenseDAO.findByName(tempNameList.get(i)).getId(),
										expenseCategoryDAO.findByName("Temporary").getId(), tempAmountList.get(i), tempDateList.get(i), tempCommentList.get(i)));
							} else {
								expenseCategoryDAO.save(new ExpenseCategory("Temporary"));
								System.out.println(expenseCategoryDAO.findByName("Temporary").getId());
								expenseDAO.save(
										new Expense(tempNameList.get(i), expenseCategoryDAO.findByName("Temporary").getId()));
								outcomeDao.save(new Outcome(expenseDAO.findByName(tempNameList.get(i)).getId(),
										expenseCategoryDAO.findByName("Temporary").getId(), tempAmountList.get(i), tempDateList.get(i), tempCommentList.get(i)));
							}
						}
				} else {
					Expense expense = expenseDAO.findByName(tempNameList.get(i));
					outcomeDao.save(new Outcome(expense.getId(), expense.getCategoryId(), tempAmountList.get(i), tempDateList.get(i), tempCommentList.get(i)));
				}
			}
		}
	}

	private static void expenseFromExcelUpdateAndSave(String name, double value, Date date, String comment) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add new Sub Category");

		ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Later", ButtonData.CANCEL_CLOSE);

		dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 150, 10, 10));

		Text addSubCategoryInput = new Text();
		addSubCategoryInput.setText(name);

		ChoiceBox<String> categoryCb = new ChoiceBox<>();
		loadCategories(categoryCb);

		Button addCategoryButton = new Button();
		addCategoryButton.setText("Add category");

		addCategoryButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addCategoryPopup();
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
					ExpenseCategory expenseCategory = new ExpenseCategory(addCategoryInput.getText());
					expenseCategoryDAO.save(expenseCategory);
					loadCategories(categoryCb);
				}
			}
		});

		gridPane.add(new Label("Sub Category: "), 0, 0);
		gridPane.add(addSubCategoryInput, 1, 0);
		gridPane.add(new Label("Category: "), 0, 1);
		gridPane.add(categoryCb, 1, 1);
		gridPane.add(addCategoryButton, 0, 2);
		dialog.getDialogPane().setContent(gridPane);

		Optional<ButtonType> result = dialog.showAndWait();

		if (result.get().getButtonData().equals(ButtonData.OK_DONE)) {
			expenseDAO.save(new Expense(addSubCategoryInput.getText(),
					expenseCategoryDAO.findByName(categoryCb.getSelectionModel().getSelectedItem())));
			outcomeDao.save(new Outcome(expenseDAO.findByName(addSubCategoryInput.getText()).getId(),
					expenseCategoryDAO.findByName(categoryCb.getSelectionModel().getSelectedItem()).getId(), value,
					date, comment));
		} else if (result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
			if (expenseCategoryDAO.findByName("Temporary") != null) {
				expenseDAO.save(
						new Expense(addSubCategoryInput.getText(), expenseCategoryDAO.findByName("Temporary").getId()));
				outcomeDao.save(new Outcome(expenseDAO.findByName(addSubCategoryInput.getText()).getId(),
						expenseCategoryDAO.findByName("Temporary").getId(), value, date, comment));
			} else {
				expenseCategoryDAO.save(new ExpenseCategory("Temporary"));
				expenseDAO.save(
						new Expense(addSubCategoryInput.getText(), expenseCategoryDAO.findByName("Temporary").getId()));
				outcomeDao.save(new Outcome(expenseDAO.findByName(addSubCategoryInput.getText()).getId(),
						expenseCategoryDAO.findByName("Temporary").getId(), value, date, comment));
			}
		}
	}

	private static void loadCategories(ChoiceBox<String> categoryCb) {
		categoryCb.getItems().clear();
		for (ExpenseCategory category : expenseCategoryDAO.getList()) {
			categoryCb.getItems().add(category.getName());
		}

	}

}