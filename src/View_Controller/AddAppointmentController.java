package View_Controller;

import Model.Appointment;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBQuery;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class AddAppointmentController {
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDate date;
    private String startTime;
    private String endTime;

    private SimpleDateFormat sdf;
    private String startDateTime;
    private String endDateTime;
    private Date start;
    private Date end;
    private Timestamp timestamp;

    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField typeTextField;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;

    private String inputStartDate;
    private String inputEndDate;
    private Timestamp timestampStart;
    private Timestamp timestampEnd;


    public void handleSaveAppointmentButton(ActionEvent event) throws ParseException, SQLException, IOException {
        title = titleTextField.getText();
        description = descriptionTextField.getText();
        location = locationTextField.getText();
        type = typeTextField.getTypeSelector();
        date = datePicker.getValue();
        startTime = startTimeTextField.getText();
        endTime = endTimeTextField.getText();

        inputStartDate= date + " " + startTime + ":00";
        inputEndDate = date + " " + endTime + ":00";

        timestampStart = Timestamp.valueOf(inputStartDate);
        timestampEnd = Timestamp.valueOf(inputEndDate);

//        (1,1,1,'not needed','not needed','not needed','not needed','Presentation','not needed','2019-01-01 00:00:00',
//                '2019-01-01 00:00:00',),
        String insertAppointmentStatement = "INSERT INTO appointment VALUES (NULL,1,1,?,?,?, 'no contact',?, 'no url',?,?,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test')";

        DBQuery.makeQuery(insertAppointmentStatement);

        PreparedStatement psAppointment = DBQuery.getQuery();

        psAppointment.setString(1, title);
        psAppointment.setString(2, description);
        psAppointment.setString(3, location);
        psAppointment.setString(4, type);
        psAppointment.setTimestamp(5,timestampStart);
        psAppointment.setTimestamp(6, timestampEnd);

        psAppointment.execute();
        System.out.println("Add Appointment Success");

        // Clears and sets AllCustomers with updated values
        Appointment.getAllAppointments().clear();
        Appointment.setAllAppointments();

        // Refreshes customerTable view with updated customers

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Appointment");
        alert.setHeaderText("Add appointment successful.");
        alert.setContentText("New appointment has been successfully added.");

        alert.showAndWait();

        // Redirects to Main scene
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Main.fxml"));
        Scene mainScene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }



    public void handleCancelButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/Main.fxml"));
        Parent AddCustomerParent = loader.load();
        Scene AddCustomerScene = new Scene(AddCustomerParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AddCustomerScene);
        window.show();
    }
}
