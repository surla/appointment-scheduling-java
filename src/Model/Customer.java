package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private String customerName;
    private int addressId;
    private String phoneNumber;

    public Customer(String customerName, int addressId) {
        this.customerName = customerName;
        this.addressId = addressId;
        allCustomers.add(this);
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void addToAllCustomers(Customer customer) {
        allCustomers.add(customer);
    }

    public ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
}
