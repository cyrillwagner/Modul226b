package ch.csbe.calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CalendarController implements ViewController{
	
	@FXML
	protected Button backtomenu;
	
	@FXML
	private void lbacktomenu(ActionEvent event) throws Exception {
		CalendarApp.loadScene("menu");
		System.out.println("Neuen Kalender erstellen");
	}

	@Override
	public void init(Stage stage) {
	
	}
	
}
