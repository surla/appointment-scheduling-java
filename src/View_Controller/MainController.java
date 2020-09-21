package View_Controller;

import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public void handleAddCustomerButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/AddCustomer.fxml"));
        Parent AddCustomerParent = loader.load();
        Scene AddCustomerScene = new Scene(AddCustomerParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AddCustomerScene);
        window.show();
    }

    public void handleModifyCustomerButton(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/ModifyCustomer.fxml"));
        Parent AddCustomerParent = loader.load();
        Scene AddCustomerScene = new Scene(AddCustomerParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AddCustomerScene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
