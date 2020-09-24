package View_Controller;

import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DBQuery;

import javax.xml.soap.Text;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class ModifyAppointmentController {

    @FXML private Label nameLabel;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField typeTextField;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;

    private Appointment selectedAppointment;
    private Integer appointmentId;
    private Integer customerId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private String startTimeStr;
    private String endTimeStr;

    private String customerName;

    public void initData (Appointment appointment) throws IOException, SQLException {
        selectedAppointment = appointment;

        appointmentId = selectedAppointment.getAppointmentId();
        customerId = selectedAppointment.getCustomerId();
        title = selectedAppointment.getTitle();
        description = selectedAppointment.getDescription();
        location = selectedAppointment.getLocation();
        type = selectedAppointment.getType();
        start = selectedAppointment.getStart();
        end = selectedAppointment.getEnd();

        Customer.setAllCustomers();
        Customer.getAllCustomers();
        for (Customer customer: Customer.getAllCustomers()) {
            if (customer.getCustomerId() == customerId) {
                customerName = customer.getCustomerName();
            }
        }

//        // Add date from start time to date variable
//        date = start.toLocalDateTime().toLocalDate();
//
//        startTimeStr = start.toLocalDateTime().toLocalTime().toString();
//        endTimeStr = end.toLocalDateTime().toLocalTime().toString();

        // Set text for TextFields
        nameLabel.setText(customerName);
        titleTextField.setText(title);
        descriptionTextField.setText(description);
        locationTextField.setText(location);
        typeTextField.setText(type);
        datePicker.setValue(date);
        startTimeTextField.setText(String.valueOf(start));
        endTimeTextField.setText(String.valueOf(end));

    }

    public void handleUpdateAppointmentButton(ActionEvent event) throws SQLException, IOException {
        title = titleTextField.getText();
        description = descriptionTextField.getText();
        location = locationTextField.getText();
        type = typeTextField.getText();

        date = datePicker.getValue();
        String startTime = startTimeTextField.getText();
        String endTime = endTimeTextField.getText();

        String inputStartDate = date + " " + startTime + ":00";
        String inputEndDate = date + " " + endTime + ":00";

        Timestamp timestampStart = Timestamp.valueOf(inputStartDate);
        Timestamp timestampEnd = Timestamp.valueOf(inputEndDate);

        String updateAppointmentStatement = "UPDATE appointment SET title=?, description=?, location=?, type=?, start=?, end=? WHERE appointmentId=?";

        DBQuery.makeQuery(updateAppointmentStatement);

        PreparedStatement psUpdateAppointment = DBQuery.getQuery();

        psUpdateAppointment.setString(1, title);
        psUpdateAppointment.setString(2, description);
        psUpdateAppointment.setString(3, location);
        psUpdateAppointment.setString(4, type);
        psUpdateAppointment.setTimestamp(5,timestampStart);
        psUpdateAppointment.setTimestamp(6, timestampEnd);
        psUpdateAppointment.setInt(7, appointmentId);

        psUpdateAppointment.execute();
        System.out.println("Update Appointment Success");

        // Clears current list of appointments.
        Appointment.getAllAppointments().clear();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/Main.fxml"));
        Parent Parent = loader.load();
        Scene mainScene = new Scene(Parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();



        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Appointment");
        alert.setHeaderText("Updated successful.");
        alert.setContentText("Appointment has been successfully updated.");

        alert.showAndWait();
    }

    public void handleDeleteButton(ActionEvent event) throws SQLException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modify Appointment");
        alert.setHeaderText("Delete appointment");
        alert.setContentText("Do you want to delete appointment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String deleteAppointmentStatement = "DELETE appointment FROM appointment WHERE appointmentId=?";
            DBQuery.makeQuery(deleteAppointmentStatement);

            PreparedStatement psDeleteAppointment = DBQuery.getQuery();
            psDeleteAppointment.setInt(1, appointmentId);
            psDeleteAppointment.execute();

            // Clears and sets AllAppointments with updated values
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
