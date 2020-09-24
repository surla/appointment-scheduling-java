package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sun.security.krb5.internal.APOptions;

import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.logging.Filter;

public class ReportController implements Initializable {
    private ObservableList<Appointment> appointments = Appointment.getAllAppointments();
    private FilteredList<Appointment> currentUserAppointments;

    private Hashtable<String, Integer> appointmentTypes;
    private String numberOfTypes = "Number of Appointment Types: \n";
    private Integer numOfAppointments = 0;


    @FXML private Label typesLabel;
    @FXML private Label numberOfAppointmentsMonth;
    @FXML private Label userLabel;

    // Table for appointment schedule
    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML private TableColumn<Appointment, LocalTime> startTimeColumn;
    @FXML private TableColumn<Appointment, LocalTime> endTimeColumn;

    public void appointmentTypes() {
        appointmentTypes = new Hashtable<>();

        for (Appointment appointment: appointments) {
            String type = appointment.getType();
            if (appointmentTypes.contains(type)) {
                appointmentTypes.put(type, appointmentTypes.get(type) + 1);
            } else {
                appointmentTypes.put(appointment.getType(), 1);
            }
        }

        /**
         * I created a Lamdda expression here to simplify code that need to be written to iterate through appointment types
         * and add it to a string variable. This make is more readable and less prone to errors
         */
        appointmentTypes.forEach((k, v) -> {
            numberOfTypes = numberOfTypes + k + " = " + v + " ";
        });
    }

    public void numberOfAppointmentsMonth() {
        for (Appointment appointment: appointments) {
            if (appointment.getDate().getMonth().equals(LocalDate.now().getMonth())) {
                numOfAppointments++;
            }
        }
    }
    public void handleCloseButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/Main.fxml"));
        Parent Parent = loader.load();
        Scene mainScene = new Scene(Parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    public void viewUserAppointments() {
        currentUserAppointments = new FilteredList<>(Appointment.getAllAppointments(), a -> true);
        currentUserAppointments.setPredicate(appointment -> {
            // Returns true of appointment month matches current month
            if (appointment.getUserId() == User.getCurrentUser().getUserId()) {
                appointmentTableView.setItems(currentUserAppointments);
                return true;
            } else {
                return false;
            }

        });


    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.appointmentTypes();
        this.numberOfAppointmentsMonth();

        // Sets label test with number of type appointments
        typesLabel.setText(numberOfTypes);

        // Sets number of appointments in current month
        numberOfAppointmentsMonth.setText("Total Number of Appointment this month: " + numOfAppointments);

        // Set user name for schedule
        userLabel.setText(User.getCurrentUser().getUsername());

        // Populates schedule for current user that is logged in
        this.viewUserAppointments(); // Sets tableview with current user appointments
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
    }
}
