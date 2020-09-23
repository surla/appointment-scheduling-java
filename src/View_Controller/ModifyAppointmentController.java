package View_Controller;

import Model.Appointment;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DBQuery;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class ModifyAppointmentController {
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField typeTextField;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;

    private Appointment selectedAppointment;
    private Integer appointmentId;
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

        appointmentId = selectedAppointment.getAppointmentId();
        title = selectedAppointment.getTitle();
        description = selectedAppointment.getDescription();
        location = selectedAppointment.getLocation();
        type = selectedAppointment.getType();
        start = selectedAppointment.getStart();
        end = selectedAppointment.getEnd();

        System.out.println(appointmentId);

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
    public void handleDeleteButton(ActionEvent event) throws SQLException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modify Appointment");
        alert.setHeaderText("Delete appointment");
        alert.setContentText("Do you want to delete appointment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String deleteAppointmentStatement = "DELETE appointment FROM appointment WHERE appointment.appointmentId=?";
            DBQuery.makeQuery(deleteAppointmentStatement);

            PreparedStatement psDeleteAppointment = DBQuery.getQuery();
            psDeleteAppointment.setInt(1, appointmentId);
            psDeleteAppointment.execute();

            // Clears and sets AllCustomers with updated values
            Appointment.getAllAppointments().clear();
            Appointment.setAllAppointments();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/Main.fxml"));
            Parent Parent = loader.load();
            Scene mainScene = new Scene(Parent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();

            // Confirms deletion of appointment
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Delete Appointment");
            alert2.setHeaderText("Delete successful.");
            alert2.setContentText("Appointment has been successfully deleted.");

            alert2.showAndWait();


        }

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
