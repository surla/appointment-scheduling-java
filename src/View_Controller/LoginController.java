package View_Controller;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import utils.DBConnection;
import utils.DBQuery;


import java.awt.*;
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
    public void handleLoginButton() throws SQLException {

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
        } else {
            System.out.println("User not found!");
        }

    }

}
