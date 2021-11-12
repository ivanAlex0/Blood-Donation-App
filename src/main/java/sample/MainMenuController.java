package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import user.BloodBank;
import user.Scraper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends ParentController implements Initializable {

    @FXML
    private Button btnHistory;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnBack;

    private static final String HOVERED_BUTTON_STYLE_HISTORY = "-fx-text-fill: #e3dfde; -fx-background-color:  #ba340b; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 5);";
    private static final String IDLE_BUTTON_STYLE_HISTORY = "-fx-background-color:  #cf5b38; -fx-background-radius: 10";
    private static final String HOVERED_BUTTON_STYLE_NEW = "-fx-text-fill: #e3dfde; -fx-background-color: #bf0f0f; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 5);";
    private static final String IDLE_BUTTON_STYLE_NEW = "-fx-background-color: #d9472e; -fx-background-radius: 10";
    private static final String HOVERED_BUTTON_STYLE_BACK = "-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 5);";
    private static final String IDLE_BUTTON_STYLE_BACK = "-fx-background-color: #ffffff;-fx-border-color: #000000; -fx-background-radius: 10";

    public MainMenuController() {
        if (counties == null) {
            Scraper scraper = new Scraper();
            counties = scraper.webScrapingCounties();
        }
        if (bloodBanks == null) {
            bloodBanks = database.getBloodBanks(counties);

           /* for (BloodBank bloodBank : bloodBanks) {
                database.initBloodBankQuantities(bloodBanks.indexOf(bloodBank) + 1);
            }*/
        }

        if (hospitals == null) {
            hospitals = database.getHospitals(counties);
        }

        currentDonor.setAppointments(database.getUsersAppointments(currentDonor, bloodBanks, hospitals));
    }

    @FXML
    private void goToAppointmentsHistory(ActionEvent event) {
        try {
            changeScene(event, "/appointmentHistory.fxml");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    private void goBackToSignUp(ActionEvent event) {
        try {
            changeScene(event, "/signUp.fxml");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    private void goToNewAppointment(ActionEvent event) {
        try {
            changeScene(event, "/bloodBankPicker.fxml");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnHistory.setOnMouseEntered(e -> btnHistory.setStyle(HOVERED_BUTTON_STYLE_HISTORY));
        btnHistory.setOnMouseExited(e -> btnHistory.setStyle(IDLE_BUTTON_STYLE_HISTORY));

        btnNew.setOnMouseEntered(e -> btnNew.setStyle(HOVERED_BUTTON_STYLE_NEW));
        btnNew.setOnMouseExited(e -> btnNew.setStyle(IDLE_BUTTON_STYLE_NEW));

        btnBack.setOnMouseEntered(e -> btnBack.setStyle(HOVERED_BUTTON_STYLE_BACK));
        btnBack.setOnMouseExited(e -> btnBack.setStyle(IDLE_BUTTON_STYLE_BACK));
    }
}
