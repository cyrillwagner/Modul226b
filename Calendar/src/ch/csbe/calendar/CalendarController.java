package ch.csbe.calendar;

import java.awt.image.BufferedImage;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class CalendarController implements Initializable
{
	@FXML
	private Pane pane;
	@FXML
	public Pane template1_pane;
	@FXML
	protected ImageView pic1;
	@FXML
	protected ImageView pic2;
	
	
	
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
        this(YearMonth.now()) ;
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
        
        // column headers:
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
	public void template1(ActionEvent e) {
		pic1.setLayoutX(15);
		pic1.setLayoutY(15);
		pic1.setFitHeight(333.25);
		pic1.setFitWidth(444.3);
		pic2.setVisible(false);
	}
	
	@FXML
	public void template2(ActionEvent e) {
		pic1.setLayoutX(15);
		pic1.setLayoutY(23);
		pic1.setFitWidth(200);
		pic1.setFitHeight(150);
		pic2.setVisible(true);
		pic2.setLayoutX(232);
		pic2.setLayoutY(23);
		pic2.setFitWidth(200);
		pic2.setFitHeight(150);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pane.getChildren().add(view);
		
	}
}