package Main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        primaryStage.setTitle("Appointment Scheduler");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {

//        Connection conn = DBConnection.startConnection();

        DBConnection.startConnection();
//        DBQuery.setStatement(conn); // Create Statement Object
//        Statement statement = DBQuery.getStatement(); // Get Statement reference
//
//        String insertStatement = "INSERT INTO country(country"

        launch(args);
        DBConnection.closeConnection();
    }
}
