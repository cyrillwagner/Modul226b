package ch.csbe.calendar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CalendarController implements Initializable
{
	@FXML
	private Pane panejan;
	@FXML
	private Pane panefeb;
	@FXML
	private Pane panemar;
	@FXML
	private Pane paneapr;
	@FXML
	private Pane panemai;
	@FXML
	private Pane panejun;
	@FXML
	private Pane panejul;
	@FXML
	private Pane paneaug;
	@FXML
	private Pane panesep;
	@FXML
	private Pane paneokt;
	@FXML
	private Pane panenov;
	@FXML
	private Pane panedez;
	@FXML
	protected ImageView janpicture1;
	@FXML
	protected ImageView janpicture2;
	@FXML
	protected ImageView febpicture1;
	@FXML
	protected ImageView febpicture2;
	@FXML
	protected ImageView marpicture1;
	@FXML
	protected ImageView marpicture2;
	@FXML
	protected ImageView aprpicture1;
	@FXML
	protected ImageView aprpicture2;
	@FXML
	protected ImageView maipicture1;
	@FXML
	protected ImageView maipicture2;
	@FXML
	protected ImageView junpicture1;
	@FXML
	protected ImageView junpicture2;
	@FXML
	protected ImageView julpicture1;
	@FXML
	protected ImageView julpicture2;
	@FXML
	protected ImageView augpicture1;
	@FXML
	protected ImageView augpicture2;
	@FXML
	protected ImageView seppicture1;
	@FXML
	protected ImageView seppicture2;
	@FXML
	protected ImageView oktpicture1;
	@FXML
	protected ImageView oktpicture2;
	@FXML
	protected ImageView novpicture1;
	@FXML
	protected ImageView novpicture2;
	@FXML
	protected ImageView dezpicture1;
	@FXML
	protected ImageView dezpicture2;
	
	public void menu(ActionEvent e){

		Node node=(Node) e.getSource();
		  Stage stage=(Stage) node.getScene().getWindow();
		  Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("menu.fxml"));
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
	
	
private final ObjectProperty<YearMonth> month = new SimpleObjectProperty<>();
    
    private final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(Locale.getDefault());
    
    private final BorderPane view ;
    private final GridPane calendar ;
    
    public CalendarController(YearMonth month) {
        view = new BorderPane();
        view.getStyleClass().add("calendar");
        calendar = new GridPane();
        calendar.getStyleClass().add("calendar-grid");

        Label header = new Label();
        header.setMaxWidth(Double.MAX_VALUE);
        header.getStyleClass().add("calendar-header");
        
        this.month.addListener((obs, oldMonth, newMonth) -> 
            rebuildCalendar());
        
        this.locale.addListener((obs, oldLocale, newLocale) -> 
            rebuildCalendar());
        
        view.setTop(header);
        view.setCenter(calendar);
        
       view.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
        
        setMonth(month);
        
        header.textProperty().bind(Bindings.createStringBinding(() -> 
            this.month.get().format(DateTimeFormatter.ofPattern("MMMM yyyy", locale.get())), 
            this.month, this.locale));
    }
    
    public CalendarController() {
        this(YearMonth.of(2017, 1)) ;
    }
    
    public void nextMonth() {
        month.set(month.get().plusMonths(1));
    }
    
    public void previousMonth() {
        month.set(month.get().minusMonths(1));
    }
    
    private void rebuildCalendar() {
        
        calendar.getChildren().clear();
        
        WeekFields weekFields = WeekFields.of(locale.get());
        
        LocalDate first = month.get().atDay(1);
        
        int dayOfWeekOfFirst = first.get(weekFields.dayOfWeek()) ;
        
        for (int dayOfWeek = 1 ; dayOfWeek <= 7 ; dayOfWeek++) {
            LocalDate date = first.minusDays(dayOfWeekOfFirst - dayOfWeek);
            DayOfWeek day = date.getDayOfWeek() ;
            Label label = new Label(day.getDisplayName(TextStyle.SHORT_STANDALONE, locale.get()));
            label.getStyleClass().add("calendar-day-header");
            GridPane.setHalignment(label, HPos.CENTER);
            calendar.add(label, dayOfWeek - 1, 0);
        }
        
        LocalDate firstDisplayedDate = first.minusDays(dayOfWeekOfFirst - 1);
        LocalDate last = month.get().atEndOfMonth() ;
        int dayOfWeekOfLast = last.get(weekFields.dayOfWeek());
        LocalDate lastDisplayedDate = last.plusDays(7 - dayOfWeekOfLast);
        
        PseudoClass beforeMonth = PseudoClass.getPseudoClass("before-display-month");
        PseudoClass afterMonth = PseudoClass.getPseudoClass("after-display-month");
                
        for (LocalDate date = firstDisplayedDate ; ! date.isAfter(lastDisplayedDate) ; date = date.plusDays(1)) {
            Label label = new Label(String.valueOf(date.getDayOfMonth()));
            label.getStyleClass().add("calendar-cell");
            label.pseudoClassStateChanged(beforeMonth, date.isBefore(first));
            label.pseudoClassStateChanged(afterMonth, date.isAfter(last));

            GridPane.setHalignment(label, HPos.CENTER);
            
            int dayOfWeek = date.get(weekFields.dayOfWeek()) ;
            int daysSinceFirstDisplayed = (int) firstDisplayedDate.until(date, ChronoUnit.DAYS);
            int weeksSinceFirstDisplayed = daysSinceFirstDisplayed / 7 ;
            
            calendar.add(label, dayOfWeek - 1, weeksSinceFirstDisplayed + 1);
            
        }
    }
    
    public Node getView() {
        return view ;
    }

    public final ObjectProperty<YearMonth> monthProperty() {
        return this.month;
    }
    

    public final YearMonth getMonth() {
        return this.monthProperty().get();
    }
    

    public final void setMonth(final YearMonth month) {
        this.monthProperty().set(month);
    }

    public final ObjectProperty<Locale> localeProperty() {
        return this.locale;
    }
    

    public final java.util.Locale getLocale() {
        return this.localeProperty().get();
    }
    

    public final void setLocale(final java.util.Locale locale) {
        this.localeProperty().set(locale);
    }
	
    @FXML
	public void upload(MouseEvent e){
		JFileChooser choose = new JFileChooser();
		choose.showOpenDialog(null);
		File img = choose.getSelectedFile();
		ImageView pic = (ImageView) e.getSource();
		pic.setImage(new Image(img.toURI().toString()));
	}
	
    @FXML
	public void jantemplate1(ActionEvent e) {
		janpicture1.setLayoutX(14);
		janpicture1.setLayoutY(14);
		janpicture1.setFitHeight(472);
		janpicture1.setFitWidth(577);
		janpicture2.setVisible(false);
	}
	
	@FXML
	public void jantemplate2(ActionEvent e) {
		janpicture1.setLayoutX(14);
		janpicture1.setLayoutY(14);
		janpicture1.setFitWidth(290);
		janpicture1.setFitHeight(460);
		janpicture2.setVisible(true);
		janpicture2.setLayoutX(332);
		janpicture2.setLayoutY(14);
		janpicture2.setFitWidth(290);
		janpicture2.setFitHeight(460);
	}
	
	@FXML
	public void febtemplate1(ActionEvent e) {
		febpicture1.setLayoutX(14);
		febpicture1.setLayoutY(14);
		febpicture1.setFitHeight(472);
		febpicture1.setFitWidth(577);
		febpicture2.setVisible(false);
	}
	
	@FXML
	public void febtemplate2(ActionEvent e) {
		febpicture1.setLayoutX(14);
		febpicture1.setLayoutY(14);
		febpicture1.setFitWidth(290);
		febpicture1.setFitHeight(460);
		febpicture2.setVisible(true);
		febpicture2.setLayoutX(332);
		febpicture2.setLayoutY(14);
		febpicture2.setFitWidth(290);
		febpicture2.setFitHeight(460);
	}
	
	@FXML
	public void martemplate1(ActionEvent e) {
		marpicture1.setLayoutX(14);
		marpicture1.setLayoutY(14);
		marpicture1.setFitHeight(472);
		marpicture1.setFitWidth(577);
		marpicture2.setVisible(false);
	}
	
	@FXML
	public void martemplate2(ActionEvent e) {
		marpicture1.setLayoutX(14);
		marpicture1.setLayoutY(14);
		marpicture1.setFitWidth(290);
		marpicture1.setFitHeight(460);
		marpicture2.setVisible(true);
		marpicture2.setLayoutX(332);
		marpicture2.setLayoutY(14);
		marpicture2.setFitWidth(290);
		marpicture2.setFitHeight(460);
	}
	
	@FXML
	public void aprtemplate1(ActionEvent e) {
		aprpicture1.setLayoutX(14);
		aprpicture1.setLayoutY(14);
		aprpicture1.setFitHeight(472);
		aprpicture1.setFitWidth(577);
		aprpicture2.setVisible(false);
	}
	
	@FXML
	public void aprtemplate2(ActionEvent e) {
		aprpicture1.setLayoutX(14);
		aprpicture1.setLayoutY(14);
		aprpicture1.setFitWidth(290);
		aprpicture1.setFitHeight(460);
		aprpicture2.setVisible(true);
		aprpicture2.setLayoutX(332);
		aprpicture2.setLayoutY(14);
		aprpicture2.setFitWidth(290);
		aprpicture2.setFitHeight(460);
	}
	
	@FXML
	public void maitemplate1(ActionEvent e) {
		maipicture1.setLayoutX(14);
		maipicture1.setLayoutY(14);
		maipicture1.setFitHeight(472);
		maipicture1.setFitWidth(577);
		maipicture2.setVisible(false);
	}
	
	@FXML
	public void maitemplate2(ActionEvent e) {
		maipicture1.setLayoutX(14);
		maipicture1.setLayoutY(14);
		maipicture1.setFitWidth(290);
		maipicture1.setFitHeight(460);
		maipicture2.setVisible(true);
		maipicture2.setLayoutX(332);
		maipicture2.setLayoutY(14);
		maipicture2.setFitWidth(290);
		maipicture2.setFitHeight(460);
	}
	
	@FXML
	public void juntemplate1(ActionEvent e) {
		junpicture1.setLayoutX(14);
		junpicture1.setLayoutY(14);
		junpicture1.setFitHeight(472);
		junpicture1.setFitWidth(577);
		junpicture2.setVisible(false);
	}
	
	@FXML
	public void juntemplate2(ActionEvent e) {
		junpicture1.setLayoutX(14);
		junpicture1.setLayoutY(14);
		junpicture1.setFitWidth(290);
		junpicture1.setFitHeight(460);
		junpicture2.setVisible(true);
		junpicture2.setLayoutX(332);
		junpicture2.setLayoutY(14);
		junpicture2.setFitWidth(290);
		junpicture2.setFitHeight(460);
	}
	
	@FXML
	public void jultemplate1(ActionEvent e) {
		julpicture1.setLayoutX(14);
		julpicture1.setLayoutY(14);
		julpicture1.setFitHeight(472);
		julpicture1.setFitWidth(577);
		julpicture2.setVisible(false);
	}
	
	@FXML
	public void jultemplate2(ActionEvent e) {
		julpicture1.setLayoutX(14);
		julpicture1.setLayoutY(14);
		julpicture1.setFitWidth(290);
		julpicture1.setFitHeight(460);
		julpicture2.setVisible(true);
		julpicture2.setLayoutX(332);
		julpicture2.setLayoutY(14);
		julpicture2.setFitWidth(290);
		julpicture2.setFitHeight(460);
	}
	
	@FXML
	public void augtemplate1(ActionEvent e) {
		augpicture1.setLayoutX(14);
		augpicture1.setLayoutY(14);
		augpicture1.setFitHeight(472);
		augpicture1.setFitWidth(577);
		augpicture2.setVisible(false);
	}
	
	@FXML
	public void augtemplate2(ActionEvent e) {
		augpicture1.setLayoutX(14);
		augpicture1.setLayoutY(14);
		augpicture1.setFitWidth(290);
		augpicture1.setFitHeight(460);
		augpicture2.setVisible(true);
		augpicture2.setLayoutX(332);
		augpicture2.setLayoutY(14);
		augpicture2.setFitWidth(290);
		augpicture2.setFitHeight(460);
	}
	
	@FXML
	public void septemplate1(ActionEvent e) {
		seppicture1.setLayoutX(14);
		seppicture1.setLayoutY(14);
		seppicture1.setFitHeight(472);
		seppicture1.setFitWidth(577);
		seppicture2.setVisible(false);
	}
	
	@FXML
	public void septemplate2(ActionEvent e) {
		seppicture1.setLayoutX(14);
		seppicture1.setLayoutY(14);
		seppicture1.setFitWidth(290);
		seppicture1.setFitHeight(460);
		seppicture2.setVisible(true);
		seppicture2.setLayoutX(332);
		seppicture2.setLayoutY(14);
		seppicture2.setFitWidth(290);
		seppicture2.setFitHeight(460);
	}
	
	@FXML
	public void okttemplate1(ActionEvent e) {
		oktpicture1.setLayoutX(14);
		oktpicture1.setLayoutY(14);
		oktpicture1.setFitHeight(472);
		oktpicture1.setFitWidth(577);
		oktpicture2.setVisible(false);
	}
	
	@FXML
	public void okttemplate2(ActionEvent e) {
		oktpicture1.setLayoutX(14);
		oktpicture1.setLayoutY(14);
		oktpicture1.setFitWidth(290);
		oktpicture1.setFitHeight(460);
		oktpicture2.setVisible(true);
		oktpicture2.setLayoutX(332);
		oktpicture2.setLayoutY(14);
		oktpicture2.setFitWidth(290);
		oktpicture2.setFitHeight(460);
	}
	
	@FXML
	public void novtemplate1(ActionEvent e) {
		novpicture1.setLayoutX(14);
		novpicture1.setLayoutY(14);
		novpicture1.setFitHeight(472);
		novpicture1.setFitWidth(577);
		novpicture2.setVisible(false);
	}
	
	@FXML
	public void novtemplate2(ActionEvent e) {
		novpicture1.setLayoutX(14);
		novpicture1.setLayoutY(14);
		novpicture1.setFitWidth(290);
		novpicture1.setFitHeight(460);
		novpicture2.setVisible(true);
		novpicture2.setLayoutX(332);
		novpicture2.setLayoutY(14);
		novpicture2.setFitWidth(290);
		novpicture2.setFitHeight(460);
	}
	
	@FXML
	public void deztemplate1(ActionEvent e) {
		dezpicture1.setLayoutX(14);
		dezpicture1.setLayoutY(14);
		dezpicture1.setFitHeight(472);
		dezpicture1.setFitWidth(577);
		dezpicture2.setVisible(false);
	}
	
	@FXML
	public void deztemplate2(ActionEvent e) {
		dezpicture1.setLayoutX(14);
		dezpicture1.setLayoutY(14);
		dezpicture1.setFitWidth(290);
		dezpicture1.setFitHeight(460);
		dezpicture2.setVisible(true);
		dezpicture2.setLayoutX(332);
		dezpicture2.setLayoutY(14);
		dezpicture2.setFitWidth(290);
		dezpicture2.setFitHeight(460);
	}	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		panejan.getChildren().add(view);
		/*panefeb.getChildren().add(view); nextMonth();
		panemar.getChildren().add(view); nextMonth();	
		panefeb.getChildren().add(view); nextMonth();
		panemar.getChildren().add(view); nextMonth();
		paneapr.getChildren().add(view); nextMonth();
		panemai.getChildren().add(view); nextMonth();
		panejun.getChildren().add(view); nextMonth();
		panejul.getChildren().add(view); nextMonth();
		paneaug.getChildren().add(view); nextMonth();
		panesep.getChildren().add(view); nextMonth();
		paneokt.getChildren().add(view); nextMonth();
		panenov.getChildren().add(view); nextMonth();
		panedez.getChildren().add(view); nextMonth();*/
	}

}