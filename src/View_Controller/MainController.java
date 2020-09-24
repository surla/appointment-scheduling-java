package View_Controller;

import Model.Appointment;

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
import sun.lwawt.macosx.CSystemTray;
import utils.DBQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;


import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;

public class MainController implements Initializable {

    private ToggleGroup viewByToggleGroup = new ToggleGroup();

    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    private FilteredList<Appointment> viewByMonthAppointments;
    private FilteredList<Appointment> viewByWeekAppointments;

    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML private TableColumn<Appointment, LocalTime> startTimeColumn;
    @FXML private TableColumn<Appointment, LocalTime> endTimeColumn;

    @FXML private RadioButton viewAllRadioButton;
    @FXML private RadioButton viewByMonthRadioButton;
    @FXML private RadioButton viewByWeekRadioButton;

    // This method shows filtered view when radio button is selecte
    public void setFilteredListView(ActionEvent event) throws SQLException {

        viewByMonthAppointments = new FilteredList<>(Appointment.getAllAppointments(), a -> true);
        viewByWeekAppointments = new FilteredList<>(Appointment.getAllAppointments(), a -> true);

        if (this.viewByToggleGroup.getSelectedToggle().equals(this.viewAllRadioButton)) {
            // Clears and sets AllCustomers with updated values
            Appointment.getAllAppointments().clear();
            Appointment.setAllAppointments();
            appointmentTableView.refresh();
        }

        if (this.viewByToggleGroup.getSelectedToggle().equals(this.viewByMonthRadioButton)) {

            try{
                viewByMonthAppointments.setPredicate(appointment -> {
                    // Returns true of appointment month matches current month
                    if (appointment.getDate().getMonth().equals(LocalDate.now().getMonth())) {
                        appointmentTableView.setItems(viewByMonthAppointments);
                        appointmentTableView.refresh();
                        return true;
                    } else {
                        return false;
                    }

                });
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

        if (this.viewByToggleGroup.getSelectedToggle().equals(this.viewByWeekRadioButton)) {

            // Gets current with by finding previous Saturday and next Saturday of current date
            LocalDate today = LocalDate.now();
            LocalDate prevSunday = today.with(previous(SATURDAY));
            LocalDate nextSaturday = today.with(next(SATURDAY));

            /**
             * I created a lambda here to have .setPredicate() method call a function to verify conditions for
             * viewing appointments of current week; Lambda is useful here because it is only called when user
             * clicks on a radio button.
             */
            // Finds appointments between first and last Sunday
            viewByWeekAppointments.setPredicate(appointment -> {

                if (appointment.getDate().isAfter(prevSunday) && appointment.getDate().isBefore(nextSaturday)) {
                    appointmentTableView.setItems(viewByWeekAppointments);
                    appointmentTableView.refresh();
                    return true;
                } else {
                    return false;
                }
            });
        }
    }

    public void handleAddAppointmentButton(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/AddAppointment.fxml"));
        Parent AddAppointmentParent = loader.load();
        Scene AddAppointmentScene = new Scene(AddAppointmentParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AddAppointmentScene);
        window.show();
    }

    public void handleModifyAppointmentButton(ActionEvent event) throws IOException, SQLException {
        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/ModifyAppointment.fxml"));
            Parent modifyAppointmentParent = loader.load();
            Scene modifyAppointmentScene = new Scene(modifyAppointmentParent);

            //Accesses the controller and passed in the selected part
            ModifyAppointmentController controller = loader.getController();
            controller.initData(appointmentTableView.getSelectionModel().getSelectedItem());

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(modifyAppointmentScene);
            window.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Appointment");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select appointment to modify.");

            alert.showAndWait();
        }
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

    public void handleReportButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/Report.fxml"));
        Parent Parent = loader.load();
        Scene mainScene = new Scene(Parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    /**
     * Checks if there is an available appointment within 15 minutes
     */

    public void availableAppointment() throws SQLException {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime currentTimePlus15 = currentTime.plusMinutes(15);
        // Check if there are no appointment for the current day.
        if (!allAppointments.contains(currentTime.toLocalDate().toString())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment");
            alert.setHeaderText("Appointment available in the next 15 minutes.");
            alert.setContentText("Please add appointment right away.");

            alert.showAndWait();
        } else {

            String selectStatement = "Select * FROM appointment";

            DBQuery.makeQuery(selectStatement);

            PreparedStatement ps = DBQuery.getQuery();
            ps.execute();

            ResultSet rs = ps.getResultSet();

            try {
                while (rs.next()) {
                    LocalDateTime appointmentStartTime = rs.getTimestamp("start").toLocalDateTime();

                    if (currentTimePlus15.isBefore(appointmentStartTime)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Add Appointment");
                        alert.setHeaderText("New appointment overlaps with another appointment");
                        alert.setContentText("Please try again.");

                        alert.showAndWait();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
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

        allAppointments = Appointment.getAllAppointments();

        //TableView Appointment
        appointmentTableView.setItems(allAppointments);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

        // Sets radio buttons to a toggle group
        this.viewAllRadioButton.setToggleGroup(viewByToggleGroup);
        this.viewByMonthRadioButton.setToggleGroup(viewByToggleGroup);
        this.viewByWeekRadioButton.setToggleGroup(viewByToggleGroup);

        // view all radio button set to default
        viewAllRadioButton.setSelected(true);

    }
}
