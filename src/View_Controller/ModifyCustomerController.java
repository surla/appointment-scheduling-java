package View_Controller;

import Model.Customer;
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
import utils.DBQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> phoneNumberColumn;
    @FXML private TableColumn<Customer, Integer> customerIdColumn;
    @FXML private TableColumn<Customer, Integer> addressIdColumn;


    @FXML private TextField customerNameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField phoneNumberTextField;

    private String customerName;
    private String address;
    private String phoneNumber;
    private int customerId;
    private int addressId;

    // Method updates customer information
    public void handleUpdateButton(ActionEvent event) throws SQLException {
        customerName = customerNameTextField.getText();
        address = addressTextField.getText();
        phoneNumber = phoneNumberTextField.getText();

        /**
         * This updates the customerName in the Customer table
         */
        String updateCustomerStatement = "UPDATE customer SET customerName=? WHERE customerId=? ";
        DBQuery.makeQuery(updateCustomerStatement);

        PreparedStatement psCustomer = DBQuery.getQuery();
        psCustomer.setString(1, customerName);
        psCustomer.setInt(2, customerId);
        psCustomer.execute();

        /**
         * This updates the address and phone fields of the Address table.
         */
        String updateAddressStatement = "UPDATE address SET address=?, phone=? WHERE address.addressId=?";
        DBQuery.makeQuery(updateAddressStatement);

        PreparedStatement psAddress = DBQuery.getQuery();
        psAddress.setString(1, address);
        psAddress.setString(2, phoneNumber);
        psAddress.setInt(3, addressId);

        psAddress.execute();


        // Clears and sets AllCustomers with updated values
        Customer.getAllCustomers().clear();
        Customer.setAllCustomers();

        // Refreshes customerTable view with updated customers
        customerTableView.refresh();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Customer");
        alert.setHeaderText("Updated successful.");
        alert.setContentText("Customer information has been successfully updated.");

        alert.showAndWait();
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

    // Method will delete customer and associated address
    public void handleDeleteButton(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modify Customer");
        alert.setHeaderText("Delete customer");
        alert.setContentText("Do you want to delete customer?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String deleteCustomerStatement = "DELETE customer, address FROM customer INNER JOIN address on address.addressId=? WHERE customer.customerId=?";
            DBQuery.makeQuery(deleteCustomerStatement);

            PreparedStatement psDeleteCustomer = DBQuery.getQuery();
            psDeleteCustomer.setInt(1, addressId);
            psDeleteCustomer.setInt(2, customerId);
            psDeleteCustomer.execute();

            // Clears and sets AllCustomers with updated values
            Customer.getAllCustomers().clear();
            Customer.setAllCustomers();

            // Refreshes customerTable view with updated customers
            customerTableView.refresh();

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Delete Customer");
            alert2.setHeaderText("Delete successful.");
            alert2.setContentText("Customer information has been successfully deleted.");

            alert2.showAndWait();
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Statement prevents running setAllCustomers() when reloading the modify scene.
        if (Customer.getAllCustomers().isEmpty()) {
            try {
                Customer.setAllCustomers();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //TableView Customer
        customerTableView.setItems(Customer.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressIdColumn.setCellValueFactory(new PropertyValueFactory<>("addressId"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Populated customer information after selection in customerTableView
        customerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
                customerNameTextField.setText(selectedCustomer.getCustomerName());
                addressTextField.setText(selectedCustomer.getAddress());
                phoneNumberTextField.setText(selectedCustomer.getPhone());

                customerId = selectedCustomer.getCustomerId();
                addressId = selectedCustomer.getAddressId();
            }
        });
    }

}
