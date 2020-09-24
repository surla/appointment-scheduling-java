package View_Controller;

import Model.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBQuery;


import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label titleLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Button logInButton;

    // Stores username and password
    private static String username;
    private static String password;
    private static Integer userId;

    private User currentUser;

    private String alertTitle;
    private String alertHeaderText;
    private String alertContentText;

    // Database connection
    private static Connection conn = null;


    // Creates and adds user log ins to log file
    public void userLog(User currentUser) throws IOException {

        String filename = "userLog";

        /**
         * Used try with resource exception control for writing log file;
         */

        try (FileWriter writeFile = new FileWriter(filename, true)) {
            PrintWriter logFile = new PrintWriter(writeFile);

            logFile.println("User: " + currentUser.getUsername() + ", Login time: " + LocalDateTime.now());

            logFile.close();
        }
    }

    /**
     * Method checks if there are appointments within 15 minutes of logging in
     *
     */

    public void checkAppointmentWithin15() throws SQLException {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime currentTimePlus15 = currentTime.plusMinutes(15);

        String selectStatement = "Select * FROM appointment";

        DBQuery.makeQuery(selectStatement);

        PreparedStatement ps = DBQuery.getQuery();
        ps.execute();

        ResultSet rs = ps.getResultSet();

        try {
            while (rs.next()) {
                LocalDateTime appointmentStartTime = rs.getTimestamp("start").toLocalDateTime();

                if (currentTime.toLocalDate().equals(appointmentStartTime.toLocalDate()) &&
                        appointmentStartTime.isBefore(currentTimePlus15) && appointmentStartTime.isAfter(currentTime)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Next Appointment");
                    alert.setHeaderText("Next appointment is in the next 15 minutes.");
                    alert.setContentText("Please be ready for appointment.");

                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    // Handles LoginButton click
    public void handleLogInButton(ActionEvent event) throws SQLException, IOException {

        username = usernameTextField.getText();
        password = passwordTextField.getText();

        String selectStatement = "Select * FROM user WHERE userName=? and password=?";

        DBQuery.makeQuery(selectStatement);

        PreparedStatement ps = DBQuery.getQuery();
        ps.setString(1, username);
        ps.setString(2, password);

        ps.execute();

        ResultSet rs = ps.getResultSet();


        /**
         * Used Try and catch exception control for user login. User gets alert if user and password is incorrect.
         */

        try {
            rs.next();
            currentUser = new User(rs.getInt(1), username);
            User.setCurrentUser(currentUser);

            // Logs user
            this.userLog(currentUser);

            // Checks if appointment is available when user logs in
            this.checkAppointmentWithin15();

            Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Main.fxml"));
            Scene mainScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertTitle);
            alert.setHeaderText(alertHeaderText);
            alert.setContentText(alertContentText);

            alert.showAndWait();
        }
    }



    public void initialize() {
        // Gets System default language
        ResourceBundle rb = ResourceBundle.getBundle("utils/language", Locale.getDefault());

        // Set language for log in scene
        titleLabel.setText(rb.getString("title"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        logInButton.setText(rb.getString("logIn"));

        alertTitle = rb.getString("alertTitle");
        alertHeaderText = rb.getString("alertHeaderText");
        alertContentText = rb.getString("alertContentText");
    }
}
