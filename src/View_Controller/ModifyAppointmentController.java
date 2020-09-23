package View_Controller;

import Model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ModifyAppointmentController {
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField typeTextField;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;

    private Appointment selectedAppointment;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDate date;
    private Timestamp start;
    private Timestamp end;
    private String startTimeStr;
    private String endTimeStr;

    public void initData (Appointment appointment) throws IOException {
        selectedAppointment = appointment;

        System.out.println(selectedAppointment);

        title = selectedAppointment.getTitle();
        description = selectedAppointment.getDescription();
        location = selectedAppointment.getLocation();
        type = selectedAppointment.getType();
        start = selectedAppointment.getStart();
        end = selectedAppointment.getEnd();

        // Add date from start time to date variable
        date = start.toLocalDateTime().toLocalDate();

        startTimeStr = start.toLocalDateTime().toLocalTime().toString();
        endTimeStr = end.toLocalDateTime().toLocalTime().toString();




        // Set text for TextFields
        titleTextField.setText(title);
        descriptionTextField.setText(description);
        locationTextField.setText(location);
        typeTextField.setText(type);
        datePicker.setValue(date);
        startTimeTextField.setText(startTimeStr);
        endTimeTextField.setText(endTimeStr);

    }

    public void handleCancelButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/Main.fxml"));
        Parent Parent = loader.load();
        Scene mainScene = new Scene(Parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
}
