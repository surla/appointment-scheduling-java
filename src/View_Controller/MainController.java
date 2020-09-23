package View_Controller;

import Model.Appointment;

import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> contactColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML private TableColumn<Appointment, LocalTime> startTimeColumn;
    @FXML private TableColumn<Appointment, LocalTime> endTimeColumn;

    public void handleAddAppointmentButton(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/AddAppointment.fxml"));
        Parent AddCustomerParent = loader.load();
        Scene AddCustomerScene = new Scene(AddCustomerParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AddCustomerScene);
        window.show();
    }

    public void handleAddCustomerButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/AddCustomer.fxml"));
        Parent AddCustomerParent = loader.load();
        Scene AddCustomerScene = new Scene(AddCustomerParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AddCustomerScene);
        window.show();
    }

    public void handleModifyCustomerButton(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/ModifyCustomer.fxml"));
        Parent AddCustomerParent = loader.load();
        Scene AddCustomerScene = new Scene(AddCustomerParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AddCustomerScene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Statement prevents running setAllCustomers() when reloading the modify scene.
        if (Appointment.getAllAppointments().isEmpty()) {
            try {
                Appointment.setAllAppointments();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        //TableView Appointment
        appointmentTableView.setItems(Appointment.getAllAppointments());
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

    }
}
