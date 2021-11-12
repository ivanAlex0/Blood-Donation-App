package sample;

import database.DatabaseOperations;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.BloodBank;
import user.County;
import user.Donor;
import user.Hospital;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * ParentController - a class that acts like a parent for every controller, implementing some general methods
 */

public class ParentController {

    public final DatabaseOperations database = new DatabaseOperations();
    public static Donor currentDonor;
    public static ArrayList<BloodBank> bloodBanks = null;
    public static ArrayList<County> counties = null;
    public static ArrayList<Hospital> hospitals = null;
    public static BloodBank currentBloodBank = null;
    public static Timestamp currentTimeStamp = null;
    public static Hospital currentHospital = null;

    public void changeScene(ActionEvent event, String fxmlFilePath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlFilePath));
        Scene  scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
