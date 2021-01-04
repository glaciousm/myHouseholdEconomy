package gr.mmam.myHouseholdEconomy.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class Stacktrace {
	
	public static void print(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText(e.getMessage());
 
        VBox dialogPaneContent = new VBox();
 
        Label label = new Label("Stack Trace:");
 
        String stackTrace = Stacktrace.getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);
 
        dialogPaneContent.getChildren().addAll(label, textArea);
 
        // Set content for Dialog Pane
        alert.getDialogPane().setContent(dialogPaneContent);
 
        alert.showAndWait();
	}
	
	private static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }

}
