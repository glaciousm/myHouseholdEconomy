package gr.mmam.myHouseholdEconomy;

import java.io.IOException;
import java.net.URL;

import gr.mmam.myHouseholdEconomy.util.DateUtil;
import gr.mmam.myHouseholdEconomy.util.Stacktrace;
import gr.mmam.myHouseholdEconomy.view.CapitalController;
import gr.mmam.myHouseholdEconomy.view.CategoriesListController;
import gr.mmam.myHouseholdEconomy.view.IncomeListController;
import gr.mmam.myHouseholdEconomy.view.MainController;
import gr.mmam.myHouseholdEconomy.view.OptionsController;
import gr.mmam.myHouseholdEconomy.view.OverviewController;
import gr.mmam.myHouseholdEconomy.view.StatisticsController;
import gr.mmam.myHouseholdEconomy.view.TransactionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class MainApp extends Application {

	public static final String APPLICATION_ICON = "http://www.pvhc.net/img132/elthivsxsyzevtsgosxf.jpg";

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Household Economy App");

		initRootLayout();
		System.out.println("Done init Root Layout");
		showOverview(DateUtil.getMonthNumber(), DateUtil.getYear());
		System.out.println("Done Overview Layout");
	}
	

	public void initRootLayout() {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		primaryStage.setX(bounds.getMinX());
		primaryStage.setY(bounds.getMinY());
		primaryStage.setWidth(bounds.getWidth());
		primaryStage.setHeight(bounds.getHeight());
		primaryStage.getIcons().add(new Image(APPLICATION_ICON));
		primaryStage.initStyle(StageStyle.DECORATED);
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			URL loc = getClass().getResource("/view/MainView.fxml");
			loader.setLocation(loc);
			rootLayout = (BorderPane) loader.load();

			MainController controller = loader.getController();
			controller.setMainApp(this);

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
//			Stacktrace.print(e);
			e.printStackTrace();
		}
	}

	public void showOverview(int month, int year) {
		try {
			FXMLLoader loader = new FXMLLoader();
			URL loc = getClass().getResource("/view/OverView.fxml");
			System.out.println(loc);
			loader.setLocation(loc);
			AnchorPane overviewView = loader.load();
			URL cssLoc = getClass().getResource("/css/overviewCSS.css");
			System.out.println("CSS: " + cssLoc);
			overviewView.getStylesheets().add(cssLoc.toExternalForm());

			rootLayout.setCenter(overviewView);

			OverviewController controller = loader.getController();
			controller.setMainApp(this);
			controller.setMonth(month);
			controller.setYear(year);
			controller.initialize();
		} catch (IOException e) {
//			Stacktrace.print(e);
			e.printStackTrace();
		}
	}

	public void showIncomeList() {
		try {
			FXMLLoader loader = new FXMLLoader();
			URL loc = getClass().getResource("/view/IncomeListView.fxml");
			System.out.println(loc);
			if (loc == null) {
				loc = getClass().getResource("resources/view/IncomeListView.fxml");
			}
			loader.setLocation(loc);
			AnchorPane incomeListView = loader.load();

			rootLayout.setCenter(incomeListView);

			IncomeListController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			Stacktrace.print(e);
		}
	}

	public void showCategoryList() {
		try {
			FXMLLoader loader = new FXMLLoader();
			URL loc = getClass().getResource("/view/CategoriesListView.fxml");
			System.out.println(loc);
			if (loc == null) {
				loc = getClass().getResource("resources/view/CategoriesListView.fxml");
			}
			loader.setLocation(loc);
			AnchorPane categoriesListView = loader.load();

			rootLayout.setCenter(categoriesListView);

			CategoriesListController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			Stacktrace.print(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void showExpense(int month, int year) {
		try {
			FXMLLoader loader = new FXMLLoader();
			URL loc = getClass().getResource("/view/TransactionsViewNew.fxml");
			System.out.println(loc);
			if (loc == null) {
				loc = getClass().getResource("resources/view/TransactionsViewNew.fxml");
			}
			loader.setLocation(loc);
			AnchorPane expenseView = loader.load();
			URL cssLoc = getClass().getResource("/css/expenses.css");
			if (cssLoc == null) {
				cssLoc = getClass().getResource("resources/css/expenses.css");
			}
			expenseView.getStylesheets().add(cssLoc.toExternalForm());

			rootLayout.setCenter(expenseView);
			TransactionController controller = loader.getController();
			controller.setMainApp(this);
			controller.setMonth(month);
			controller.setYear(year);
			controller.initialize();
		} catch (IOException e) {
			Stacktrace.print(e);
		}
	}

	public void showCapital() {
		try {
			FXMLLoader loader = new FXMLLoader();
			URL loc = getClass().getResource("/view/CapitalView.fxml");
			System.out.println(loc);
			if (loc == null) {
				loc = getClass().getResource("resources/view/CapitalView.fxml");
			}
			loader.setLocation(loc);
			AnchorPane capitalView = loader.load();

			rootLayout.setCenter(capitalView);

			CapitalController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			Stacktrace.print(e);
		}
	}
	
	public void showOptions() {
		try {
			FXMLLoader loader = new FXMLLoader();
			URL loc = getClass().getResource("/view/OptionsView.fxml");
			System.out.println(loc);
			if (loc == null) {
				loc = getClass().getResource("resources/view/OptionsView.fxml");
			}
			loader.setLocation(loc);
			AnchorPane optionsView = loader.load();

			rootLayout.setCenter(optionsView);

			OptionsController controller = loader.getController();
			controller.setMainApp(this);
			controller.setStage(primaryStage);
		} catch (IOException e) {
			Stacktrace.print(e);
		}
	}
	
	public void showStatistics(int month, int year) {
		try {
			FXMLLoader loader = new FXMLLoader();
			URL loc = getClass().getResource("/view/StatisticsView.fxml");
			System.out.println(loc);
			if (loc == null) {
				loc = getClass().getResource("resources/view/StatisticsView.fxml");
			}
			loader.setLocation(loc);
			AnchorPane statisticsView = loader.load();
			URL cssLoc = getClass().getResource("/css/overviewCSS.css");
			if (cssLoc == null) {
				cssLoc = getClass().getResource("resources/css/overviewCSS.css");
			}
			statisticsView.getStylesheets().add(cssLoc.toExternalForm());

			rootLayout.setCenter(statisticsView);

			StatisticsController controller = loader.getController();
			controller.setMainApp(this);
			controller.setMonth(month);
			controller.setYear(year);
                        controller.initialize();
		} catch (IOException e) {
			Stacktrace.print(e);
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
