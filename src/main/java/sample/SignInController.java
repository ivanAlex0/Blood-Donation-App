package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import user.Donor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController extends ParentController implements Initializable {

    private static final String HOVERED_BUTTON_STYLE_REGISTER = "-fx-text-fill: #ffffff; -fx-background-color:  #2a6fb5;";
    private static final String IDLE_BUTTON_STYLE_REGISTER = "-fx-text-fill: #000000; -fx-background-color: #6aaaeb;";

    private static final String HOVERED_BUTTON_STYLE_SIGN_IN = "-fx-text-fill: #ffffff; -fx-background-color:  #b83930;";
    private static final String IDLE_BUTTON_STYLE_SIGN_IN = "-fx-text-fill: #ffffff; -fx-background-color:  #e8574d;";

    @FXML private Label lblEmailSignIn;
    @FXML private Label lblPasswordSignIn;
    @FXML private TextField txtEmailSignIn;
    @FXML private PasswordField passPasswordSignIn;
    @FXML private AnchorPane anchorAlert;
    @FXML private Button btnAlert;
    @FXML private Label lblAlert;
    @FXML private Button btnRegister2;
    @FXML private Button btnSignIn2;

    public void signIn(ActionEvent event) {
        clearSignInLabels();
        if (txtEmailSignIn.getText().isEmpty()) {
            lblEmailSignIn.setText("Please enter your email.");
            return;
        }
        if (passPasswordSignIn.getText().isEmpty()) {
            lblPasswordSignIn.setText("Please enter your password.");
            return;
        }
        Donor donor = new Donor(txtEmailSignIn.getText(), passPasswordSignIn.getText());
        if (donor.isEmailValid()) {
            if (database.isDonorInDatabaseSignIn(donor)) {
                currentDonor = database.getDonorData(donor.getEmail());
                try {
                    changeScene(event, "/mainMenu.fxml");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                if (database.isEmailInDatabase(donor)) {
                    anchorAlert.setVisible(true);
                    lblAlert.setText("The password is incorrect. Try again.");
                } else {
                    anchorAlert.setVisible(true);
                    lblAlert.setText("This email is not registered. Please create a new account.");
                }
            }
        } else {
            lblEmailSignIn.setText("This email is not valid.");
        }
    }

    public void goToSignUp(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "/signUp.fxml");
    }

    public void closeAlert() {
        anchorAlert.setVisible(false);
    }

    public void clearSignInLabels() {
        lblEmailSignIn.setText("");
        lblPasswordSignIn.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSignIn2.setOnMouseEntered(e -> btnSignIn2.setStyle(HOVERED_BUTTON_STYLE_SIGN_IN));
        btnSignIn2.setOnMouseExited(e -> btnSignIn2.setStyle(IDLE_BUTTON_STYLE_SIGN_IN));

        btnRegister2.setOnMouseEntered(e -> btnRegister2.setStyle(HOVERED_BUTTON_STYLE_REGISTER));
        btnRegister2.setOnMouseExited(e -> btnRegister2.setStyle(IDLE_BUTTON_STYLE_REGISTER));
    }
}
