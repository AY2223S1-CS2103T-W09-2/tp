package seedu.address.logic;

import static javafx.scene.paint.Color.WHITE;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.spi.CalendarNameProvider;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.address.model.calendar.CalendarEvent;
import seedu.address.model.calendar.CalendarMonth;
import seedu.address.model.person.Date;
import seedu.address.ui.CalendarEventListPanel;
import seedu.address.ui.JumpText;
import seedu.address.ui.NextButton;
import seedu.address.ui.PreviousButton;
import seedu.address.ui.RefreshButton;

/**
 * The manager of the logic for the Calendar.
 */
public class CalendarLogic {
    public static final String MESSAGE_CONSTRAINTS = "Invalid date time format";

    private static final String[] MONTH_NAMES = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };
    private static final String[] DAY_NAMES = {
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };
    private static final String TEXT_HEADER_STYLE = "-fx-font-size: 15pt; -fx-text-fill: white; "
            + "-fx-background-color: #fff";
    private PreviousButton prevButton = new PreviousButton("Prev", this);
    private NextButton nextButton = new NextButton("Next", this);
    private RefreshButton refreshButton = new RefreshButton("Refresh", this);
    private JumpText jumpText = new JumpText(this);

    @FXML
    private GridPane calendarDisplay;
    @FXML
    private HBox topCalendar;
    private Stage primaryStage;
    private Logic logic;
    private Calendar currentMonth;
    private CalendarMonth calendarMonth;

    /**
     * Constructs a {@code CalendarLogic} with the given {@code Logic}, {@code Stage}
     * {@code GridPane} and {@code HBox}.
     */
    public CalendarLogic(Logic logic, Stage primaryStage, GridPane calendarDisplay, HBox topCalendar) {
        requireAllNonNull(logic, primaryStage, calendarDisplay, topCalendar);
        this.calendarDisplay = calendarDisplay;
        this.topCalendar = topCalendar;
        this.logic = logic;
        this.primaryStage = primaryStage;
    }

    /**
     * Initialises the logic components for the Calendar.
     */
    public void initialiseLogic() {
        calendarMonth = new CalendarMonth(logic.getFilteredCalendarEventList());
        currentMonth = new GregorianCalendar();
        currentMonth.set(Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * Draws the Ui for the Calendar.
     */
    public void drawCalendar() {
        drawHeader();
        drawBody();
    }

    private void drawHeader() {
        Text textHeader = getTextHeader();
        topCalendar.getChildren().addAll(textHeader, prevButton.getRoot(), nextButton.getRoot(),
                refreshButton.getRoot(), jumpText.getRoot());
        topCalendar.setMargin(textHeader, new Insets(0, 50, 0, 0));
    }

    private void drawBody() {
        // Draw days of the week
        for (int day = 1; day <= 7; day++) {
            Text tDayName = new Text(" " + getDayName(day));
            tDayName.setFill(WHITE);
            calendarDisplay.add(tDayName, day - 1, 0);
        }

        // Draw days in month
        int currentDay = currentMonth.get(Calendar.DAY_OF_MONTH);
        int daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK);
        int row = 1;
        for (int i = currentDay; i <= daysInMonth; i++) {
            if (dayOfWeek == 8) {
                dayOfWeek = 1;
                row++;
            }
            ObservableList<CalendarEvent> calendarEventsInDayOfMonth = calendarMonth
                    .getCalendarEventInDayOfMonth(currentDay, currentMonth.get(Calendar.MONTH) + 1,
                            currentMonth.get(Calendar.YEAR));

            CalendarEventListPanel calendarEventListPanel = new CalendarEventListPanel(calendarEventsInDayOfMonth,
                    primaryStage);
            VBox calendarEventList = calendarEventListPanel.getCalendarEventList(currentDay);
            calendarDisplay.add(calendarEventList, dayOfWeek - 1, row);
            currentDay++;
            dayOfWeek++;
        }
    }

    private String getDayName(int n) {
        return DAY_NAMES[n - 1];
    }

    private String getMonthName(int n) {
        return MONTH_NAMES[n];
    }

    private Text getTextHeader() {
        String monthString = getMonthName(currentMonth.get(Calendar.MONTH));
        String yearString = String.valueOf(currentMonth.get(Calendar.YEAR));
        Text header = new Text(monthString + ", " + yearString);
        header.setStyle(TEXT_HEADER_STYLE);
        header.setFill(WHITE);
        return header;
    }

    /**
     * Refreshes the CalendarEvents.
     */
    public void refresh() {
        resetGridPane();
        this.calendarMonth = new CalendarMonth(logic.getFilteredCalendarEventList());
        drawCalendar();
    }

    /**
     * Displays the CalendarEvents in the previous month.
     */
    public void previous() {
        this.calendarMonth = new CalendarMonth(logic.getFilteredCalendarEventList());
        currentMonth = getPreviousMonth(currentMonth);
        updateCalendarMonth();
    }

    /**
     * Displays the CalendarEvents in the next month.
     */
    public void next() {
        this.calendarMonth = new CalendarMonth(logic.getFilteredCalendarEventList());
        currentMonth = getNextMonth(currentMonth);
        updateCalendarMonth();
    }
    /**
     * Displays the CalendarEvents given by the user input.
     */
    public void jump() {
        this.calendarMonth = new CalendarMonth(logic.getFilteredCalendarEventList());
        currentMonth = getJumpMonth(currentMonth);
        updateCalendarMonth();
    }

    private GregorianCalendar getJumpMonth(Calendar cal) {

        String date = jumpText.getText();
        jumpText.clear();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            Date jumpDate = new Date(LocalDate.parse(date, formatter));
            int newMonth = jumpDate.getMonth() - 1;
            int newYear = jumpDate.getYear();
            return new GregorianCalendar(newYear, newMonth, 1);

        } catch(DateTimeParseException e) {
            jumpText.setText("Invalid Date Format");
        }
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);

    }

    private void updateCalendarMonth() {
        Text newMonthHeader = getTextHeader();
        topCalendar.getChildren().set(0, newMonthHeader);
        topCalendar.setMargin(newMonthHeader, new Insets(0, 50, 0, 0));
        resetCalendarBody();
        drawBody();
    }

    private GregorianCalendar getPreviousMonth(Calendar cal) {
        int prevMonth;
        int prevYear;
        int currentMonth = cal.get(Calendar.MONTH);
        //If December, reset back to January, Add a year
        if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = cal.get(Calendar.YEAR) - 1;
        } else { //else add a month, retain the year
            prevMonth = currentMonth - 1;
            prevYear = cal.get(Calendar.YEAR);
        }
        return new GregorianCalendar(prevYear, prevMonth, 1);
    }

    private GregorianCalendar getNextMonth(Calendar cal) {
        int futureMonth;
        int futureYear;
        int currentMonth = cal.get(Calendar.MONTH);
        //If January, reset back to December, decrement a year
        if (currentMonth == 11) {
            futureMonth = 0;
            futureYear = cal.get(Calendar.YEAR) + 1;
        } else { //else decrement a month, retain the year
            futureMonth = currentMonth + 1;
            futureYear = cal.get(Calendar.YEAR);
        }
        return new GregorianCalendar(futureYear, futureMonth, 1);
    }

    private void resetGridPane() {
        topCalendar.getChildren().clear();
        Node node = calendarDisplay.getChildren().get(0);
        calendarDisplay.getChildren().clear();
        calendarDisplay.getChildren().add(0, node);
    }

    private void resetCalendarBody() {
        Node node = calendarDisplay.getChildren().get(0);
        calendarDisplay.getChildren().clear();
        calendarDisplay.getChildren().add(0, node);
    }
}
