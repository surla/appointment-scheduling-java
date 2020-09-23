package View_Controller;

import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddCustomerController {
    @FXML private TextField customerNameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField phoneNumberTextField;

    private String customerName;
    private String address;
    private String phoneNumber;

    public void handleSaveCustomerButton(ActionEvent event) throws SQLException, IOException {

        // Sets current date and time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();


        customerName = customerNameTextField.getText();
        address = addressTextField.getText();
        phoneNumber = phoneNumberTextField.getText();

        // Alerts user if any fields isEmpty
        if (customerName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Customer");
            alert.setHeaderText("One or more field(s) is empty.");
            alert.setContentText("Please try again.");

            alert.showAndWait();
        } else {
            /**
             * This inserts address data into address table
             */
            String insertAddressStatement = "INSERT INTO address VALUES(NULL,?,'', 1,'11111',?,?,?,?,?)";

            DBQuery.makeQuery(insertAddressStatement, Statement.RETURN_GENERATED_KEYS);

            PreparedStatement psAddress = DBQuery.getQuery();

            psAddress.setString(1, address);
            psAddress.setString(2, phoneNumber);
            psAddress.setString(3, dtf.format((now)));
            psAddress.setString(4, "test");
            psAddress.setString(5, dtf.format((now)));
            psAddress.setString(6,"test");

            // Execute query
            psAddress.execute();
            ResultSet rs = psAddress.getGeneratedKeys();
            rs.next();
            int addressId = rs.getInt(1);


            String insertCustomerStatement = "INSERT INTO customer VALUES (NULL,?,?, 1, ?, ?, ?, ?)";

            DBQuery.makeQuery(insertCustomerStatement);
            PreparedStatement psCustomer = DBQuery.getQuery();

            psCustomer.setString(1, customerName);
            psCustomer.setInt(2, addressId);
            psCustomer.setString(3, dtf.format((now)));
            psCustomer.setString(4, "test");
            psCustomer.setString(5, dtf.format((now)));
            psCustomer.setString(6,"test");

            psCustomer.setString(1, customerName);
            psCustomer.setInt(2,addressId);
            psCustomer.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Customer");
            alert.setHeaderText("New customer successfully added.");

            alert.showAndWait();

            Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Main.fxml"));
            Scene mainScene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();

        }
    }

    public void handleCancelButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/Main.fxml"));
        Parent Parent = loader.load();
        Scene mainScene = new Scene(Parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

}
