package Model;

import static utils.DBConnection.conn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private int customerId;
    private String customerName;
    private int addressId;
    private String address;
    private String phone;

    public Customer(int customerId, String customerName, int addressId, String address, String phone) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.address = address;
        this.phone = phone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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

    public static void setAllCustomers() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT customerId, customerName, address.addressId, address, phone FROM customer INNER JOIN address WHERE customer.addressId = address.addressId");

        while (rs.next()) {
            allCustomers.add(new Customer(rs.getInt("customerId"), rs.getString("customerName"), rs.getInt("addressId"), rs.getString("address"), rs.getString("phone")));
        }
    }

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
}
