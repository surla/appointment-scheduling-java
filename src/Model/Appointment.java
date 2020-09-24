package Model;

import static utils.DBConnection.conn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;


public class Appointment {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    private int appointmentId;
    private int customerId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;

    public Appointment(int appointmentId, int customerId, String title, String description, String location, String type, LocalDate date, LocalTime start, LocalTime end) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public static void setAllAppointments() throws SQLException {

        ResultSet rs = conn.createStatement().executeQuery("SELECT appointmentId, customerId, title, description, location, type, start, end FROM appointment");

        while (rs.next()) {
            allAppointments.add(new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getString("title"), rs.getString("description"),
                    rs.getString("location"), rs.getString("type"),
                    rs.getTimestamp("start").toLocalDateTime().toLocalDate(),
                    rs.getTimestamp("start").toLocalDateTime().toLocalTime(),
                    rs.getTimestamp("end").toLocalDateTime().toLocalTime()));

            System.out.println(rs.getTimestamp("start").toLocalDateTime());
        }
    }

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }
}
