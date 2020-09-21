package Model;

import static utils.DBConnection.conn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private String customerName;
    private String address;
    private String phone;

    public Customer(String customerName, String address, String phone) {
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String addressId) {
        this.address = addressId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void addToAllCustomers(Customer customer) {
        allCustomers.add(customer);
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT customerName, address, phone FROM customer INNER JOIN address WHERE customer.addressId = address.addressId");


        while (rs.next()) {
            allCustomers.add(new Customer(rs.getString("customerName"), rs.getString("address"), rs.getString("phone")));
            System.out.println(rs.getString("phone"));
        }

        return allCustomers;
    }
}
