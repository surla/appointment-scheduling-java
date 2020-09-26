package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @FXML private ComboBox customerList;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField typeTextField;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;
    @FXML private ComboBox customerComboBox;

    private String inputStartDate;
    private String inputEndDate;
    private Timestamp timestampStart;
    private Timestamp timestampEnd;

    private ObservableList<Customer> getCustomerList = FXCollections.observableArrayList();
    private Integer customerId;
    private Integer userId;

    public boolean checkAppointment(LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        String selectStatement = "Select * FROM appointment";

        DBQuery.makeQuery(selectStatement);

        PreparedStatement ps = DBQuery.getQuery();
        ps.execute();

        ResultSet rs = ps.getResultSet();

        try {
            while (rs.next()) {
                LocalDateTime appointmentStartTime = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime appointmentEndTime = rs.getTimestamp("end").toLocalDateTime();

                if (startTime.isAfter(appointmentStartTime.minusMinutes(1)) && endTime.isBefore(appointmentEndTime.plusMinutes(1))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Add Appointment");
                    alert.setHeaderText("New appointment overlaps with another appointment");
                    alert.setContentText("Please change appointment times and try again.");

                    alert.showAndWait();
                    return true;
                }

                if (startTime.isAfter(appointmentStartTime.minusMinutes(1)) && startTime.isBefore(appointmentEndTime.plusMinutes(1)) && endTime.isAfter(appointmentEndTime.plusMinutes(1))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Add Appointment");
                    alert.setHeaderText("New appointment overlaps with another appointment");
                    alert.setContentText("Please change appointment times and try again.");

                    alert.showAndWait();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean handleSaveAppointmentButton(ActionEvent event) throws ParseException, SQLException, IOException {
        // Gets current logged in userId;
        userId= User.getCurrentUser().getUserId();


        title = titleTextField.getText();
        description = descriptionTextField.getText();
        location = locationTextField.getText();
        type = typeTextField.getText();
        date = datePicker.getValue();
        startTime = startTimeTextField.getText();
        endTime = endTimeTextField.getText();

        try {
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() ||  date.equals("") ||
                    startTime.isEmpty() || endTime.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment");
                alert.setHeaderText("One or more field(s) is empty or invalid!");
                alert.setContentText("Please try again.");

                alert.showAndWait();
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }




        for (Customer customer: getCustomerList) {
            if (customer.getCustomerName().equals(customerComboBox.getValue())) {
                customerId = customer.getCustomerId();
            }
        }

        inputStartDate= date + " " + startTime + ":00";
        inputEndDate = date + " " + endTime + ":00";

        timestampStart = Timestamp.valueOf(inputStartDate);
        timestampEnd = Timestamp.valueOf(inputEndDate);

        LocalTime timestampStartTime = timestampStart.toLocalDateTime().toLocalTime();
        LocalTime timestampEndTime = timestampEnd.toLocalDateTime().toLocalTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime businessStart = LocalDateTime.parse("2020-08-08 08:00:00", formatter);
        LocalDateTime businessEnd = LocalDateTime.parse("2020-08-08 17:00:00", formatter);

        LocalTime businessStartTime = businessStart.toLocalTime();
        LocalTime businessEndTime = businessEnd.toLocalTime();

        /**
         * Checks if new appointment times are within business hours and is formatted correctly
         */
        try {
            if (timestampStartTime.isBefore(businessStartTime) || timestampStartTime.isAfter(businessEndTime) ||
                    timestampEndTime.isBefore(businessStartTime) || timestampEndTime.isAfter(businessEndTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Appointment");
                alert.setHeaderText("Invalid appointment times!");
                alert.setContentText("Please make sure times are within business hours and are in 24 hour time.");

                alert.showAndWait();
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
        }

        /**
         * Checks if new appointment overlaps existing appointment
         */
        try {
            if (this.checkAppointment(timestampStart.toLocalDateTime(), timestampEnd.toLocalDateTime())) {
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        String insertAppointmentStatement = "INSERT INTO appointment VALUES (NULL,?,?,?,?,?, 'no contact',?, 'no url',?,?,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test')";

        DBQuery.makeQuery(insertAppointmentStatement);

        PreparedStatement psAppointment = DBQuery.getQuery();
        psAppointment.setInt(1, customerId);
        psAppointment.setInt(2, userId);
        psAppointment.setString(3, title);
        psAppointment.setString(4, description);
        psAppointment.setString(5, location);
        psAppointment.setString(6, type);
        psAppointment.setTimestamp(7,timestampStart);
        psAppointment.setTimestamp(8, timestampEnd);

        psAppointment.execute();

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

        return true;
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

    public void initialize() throws SQLException {

        // Below creates a String observablelist to populate customer combobox

        if (Customer.getAllCustomers().isEmpty()) {
            try {
                Customer.setAllCustomers();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        getCustomerList = Customer.getAllCustomers();

        ObservableList<String> customerNameList = FXCollections.observableArrayList();

        for (Customer customer: getCustomerList) {
            customerNameList.add(customer.getCustomerName());
        }

        // Sets combobox with customer names;
        customerComboBox.setItems(customerNameList);

        //Sets default customer
        customerComboBox.getSelectionModel().selectFirst();
    }

}
