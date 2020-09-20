package View_Controller;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBQuery;


import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;

    // Stores username and password
    private static String username;
    private static String password;

    // Database connection
    private static Connection conn = null;

    // Handles LoginButton click
    public void handleLoginButton(ActionEvent event) throws SQLException, IOException {

        username = usernameTextField.getText();
        password = passwordTextField.getText();

        String selectStatement = "Select * FROM user WHERE userName=? and password=?";

        DBQuery.makeQuery(selectStatement);

        PreparedStatement ps = DBQuery.getQuery();
        ps.setString(1, username);
        ps.setString(2, password);

        ps.execute();

        ResultSet rs = ps.getResultSet();

        if (rs.next()) {
            System.out.println("User found!");
            System.out.println(rs);

            Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Main.fxml"));
            Scene mainScene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        } else {
            System.out.println("User not found!");
        }

    }
}
