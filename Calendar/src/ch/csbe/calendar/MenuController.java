package ch.csbe.calendar;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController implements ViewController{

	@FXML
	protected Button newcalendar;
	@FXML
	protected Button newtemplate;
	
	@FXML
	private void lnewcalendar(ActionEvent event) throws Exception {
		CalendarApp.loadScene("calendar");
		System.out.println("Neuen Kalender erstellen");
	}
	
	@FXML
	private void lnewtemplate(ActionEvent event) throws Exception {
		System.out.println("Neues Template erstellen");
	}

	@Override
	public void init(Stage stage) {
		
	}
}
