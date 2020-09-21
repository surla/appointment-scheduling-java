package Model;

import static utils.DBConnection.conn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;


public class Appointment {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private Timestamp start;
    private Timestamp end;

    public Appointment(String title, String description, String location, String contact, String type, Timestamp start, Timestamp end) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public static void setAllAppointments() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT title, description, location, contact, type, start, end FROM appointment");

        while (rs.next()) {
            System.out.println("Getting date: " + rs.getDate("start"));
            allAppointments.add(new Appointment(rs.getString("title"), rs.getString("description"), rs.getString("location"), rs.getString("contact"), rs.getString("type"), rs.getTimestamp("start"), rs.getTimestamp("end")));
        }

        System.out.println("Set all appointments" + allAppointments);
    }

    public static ObservableList<Appointment> getAllAppointments() {
        System.out.println("Get all Appointments success!" + allAppointments);
        return allAppointments;
    }
}