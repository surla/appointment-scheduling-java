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


    public void handleSaveAppointmentButton(ActionEvent event) throws ParseException, SQLException, IOException {

        System.out.println(User.getCurrentUser().getUserId());

        for (Customer customer: getCustomerList) {
            if (customer.getCustomerName().equals(customerComboBox.getValue())) {
                customerId = customer.getCustomerId();
            }
        }

        // Gets current logged in userId;
        userId= User.getCurrentUser().getUserId();



        title = titleTextField.getText();
        description = descriptionTextField.getText();
        location = locationTextField.getText();
        type = typeTextField.getText();
        date = datePicker.getValue();
        startTime = startTimeTextField.getText();
        endTime = endTimeTextField.getText();

        inputStartDate= date + " " + startTime + ":00";
        inputEndDate = date + " " + endTime + ":00";

        timestampStart = Timestamp.valueOf(inputStartDate);
        timestampEnd = Timestamp.valueOf(inputEndDate);

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
            Customer.setAllCustomers();
        }

        getCustomerList = Customer.getAllCustomers();

        ObservableList<String> customerNameList = FXCollections.observableArrayList();

        for (Customer customer: getCustomerList) {
            customerNameList.add(customer.getCustomerName());
        }

        customerComboBox.setItems(customerNameList);
    }

}
