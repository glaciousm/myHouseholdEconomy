package gr.mmam.myHouseholdEconomy.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import gr.mmam.myHouseholdEconomy.MainApp;
import gr.mmam.myHouseholdEconomy.util.DateUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class MainController {
	@FXML
	Button statisticsBtn = new Button();
//	@FXML
//	Label yearLabel = new Label();
//	@FXML
//	Label dayLabel = new Label();
//	@FXML
//	Label januaryLabel = new Label();
//	@FXML
//	Label februaryLabel = new Label();
//	@FXML
//	Label marchLabel = new Label();
//	@FXML
//	Label aprilLabel = new Label();
//	@FXML
//	Label mayLabel = new Label();
//	@FXML
//	Label juneLabel = new Label();
//	@FXML
//	Label julyLabel = new Label();
//	@FXML
//	Label augustLabel = new Label();
//	@FXML
//	Label septemberLabel = new Label();
//	@FXML
//	Label octoberLabel = new Label();
//	@FXML
//	Label novemberLabel = new Label();
//	@FXML
//	Label decemberLabel = new Label();
	@FXML
	Text versionText = new Text();
	@FXML
	Button categoriesListBtn = new Button();
	@FXML
	Button incomeListBtn = new Button();
	@FXML
	Button expensesBtn = new Button();
	@FXML
	Button homeBtn = new Button();
//	@FXML
//	Pane toolBarPane = new Pane();
	@FXML
	Button capitalBtn = new Button();
	@FXML
	Button optionsBtn = new Button();
//	@FXML
//	ImageView overviewImg = new ImageView();
//	@FXML
//	ImageView categoriesImg = new ImageView();
//	@FXML
//	ImageView incomeImg = new ImageView();
//	@FXML
//	ImageView expensesImg = new ImageView();
//	@FXML
//	ImageView statisticsImg = new ImageView();
//	@FXML
//	ImageView capitalImg = new ImageView();
//	@FXML
	ImageView optionsImg = new ImageView();

	List<Label> months = new ArrayList<Label>();

	Properties prop = new Properties();
	InputStream input = getClass().getClassLoader().getResourceAsStream("options.properties");
	
	private MainApp mainApp;

	public MainController() {

	}

	@FXML
	private void initialize() {
		if(input == null) {
			input = getClass().getClassLoader().getResourceAsStream("options.properties");
		}
		try {
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		versionText.setText("v." + prop.getProperty("version"));
		categoriesListBtn.setPickOnBounds(true);
		categoriesListBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				event.consume();
				mainApp.showCategoryList();
			}
		});
		incomeListBtn.setPickOnBounds(true);
		incomeListBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				event.consume();
				mainApp.showIncomeList();
			}
		});
		expensesBtn.setPickOnBounds(true);
		expensesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				event.consume();
				mainApp.showExpense(DateUtil.getMonthNumber(), DateUtil.getYear());
			}
		});
		capitalBtn.setPickOnBounds(true);
		capitalBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				event.consume();
				mainApp.showCapital();
			}
		});
		optionsBtn.setPickOnBounds(true);
		optionsBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				event.consume();
				mainApp.showOptions();
			}
		});
		statisticsBtn.setPickOnBounds(true);
		statisticsBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				event.consume();
				mainApp.showStatistics(DateUtil.getMonthNumber(), DateUtil.getYear());
			}
		});
		homeBtn.setPickOnBounds(true);
		homeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				event.consume();
				mainApp.showOverview(DateUtil.getMonthNumber(), DateUtil.getYear());
			}
			
		});
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
