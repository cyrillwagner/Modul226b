package ch.csbe.calendar;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MenuController {

	public void newcalendar(ActionEvent e){

		Node node=(Node) e.getSource();
		  Stage stage=(Stage) node.getScene().getWindow();
		  Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("newcalendar.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		  Scene scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
    }

	
	public void exit (ActionEvent e){
		System.exit(0);
	}
}
