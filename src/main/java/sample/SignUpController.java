package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import user.BloodType;
import user.County;
import user.Donor;
import user.Scraper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SignUpController extends ParentController implements Initializable {
    private static final String HOVERED_BUTTON_STYLE_SIGN_IN = "-fx-text-fill: #ffffff; -fx-background-color:  #2a6fb5;";
    private static final String IDLE_BUTTON_STYLE_SIGN_IN = "-fx-text-fill: #000000; -fx-background-color: #6aaaeb;";

    private static final String HOVERED_BUTTON_STYLE_REGISTER = "-fx-text-fill: #ffffff; -fx-background-color:  #b83930;";
    private static final String IDLE_BUTTON_STYLE_REGISTER = "-fx-text-fill: #ffffff; -fx-background-color:  #e8574d;";


    @FXML private Label lblFirstName;
    @FXML private Label lblLastName;
    @FXML private Label lblEmail;
    @FXML private Label lblPassword;
    @FXML private Label lblConfirmPassword;
    @FXML private Label lblPhoneNumber;
    @FXML private Label lblDOB;
    @FXML private Label lblBloodType;
    @FXML private Label lblRH;
    @FXML private Label lblCounty;
    @FXML private Label lblCNP;
    @FXML private AnchorPane anchorAlert;
    @FXML private Button btnAlert;

    @FXML
    private ComboBox<String> comboCounty;
    private final Scraper scraper = new Scraper();
    private final ArrayList<County> counties = scraper.webScrapingCounties();
    @FXML
    private ComboBox<String> comboBloodType;
    private final ObservableList<String> bloodTypeOptions = FXCollections.observableArrayList("01", "A2", "B3", "AB4");
    @FXML
    private ComboBox<String> comboRH;
    private final ObservableList<String> RHOptions = FXCollections.observableArrayList("positive", "negative");
    @FXML private DatePicker dateDOB;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCNP;
    @FXML private TextField txtPhoneNumber;
    @FXML private PasswordField passPassword;
    @FXML private PasswordField passConfirmPassword;
    @FXML private Button btnRegister;
    @FXML private Button btnSignIn;

    public void signUp(ActionEvent event) throws IOException {
        clearErrorLabels();
        if (txtFirstName.getText().isEmpty()) {
            lblFirstName.setText("Please enter your first name.");
            return;
        }
        if (txtLastName.getText().isEmpty()) {
            lblLastName.setText("Please enter your last name.");
            return;
        }
        if (txtEmail.getText().isEmpty()) {
            lblEmail.setText("Please enter your email.");
            return;
        }
        if (passPassword.getText().isEmpty()) {
            lblPassword.setText("Please enter your password.");
            return;
        }
        if (passConfirmPassword.getText().isEmpty()) {
            lblConfirmPassword.setText("Please confirm your password.");
            return;
        }
        if (passPassword.getText().length() < 6) {
            lblPassword.setText("The password needs to have at least 6 characters.");
            return;
        }
        if (!passPassword.getText().equals(passConfirmPassword.getText())) {
            lblConfirmPassword.setText("Your passwords do not match.");
            return;
        }
        if (txtPhoneNumber.getText().isEmpty()) {
            lblPhoneNumber.setText("Please enter your phone number.");
            return;
        }
        if (dateDOB.getValue() == null) {
            lblDOB.setText("Please enter your date of birth.");
            return;
        }
        if (comboBloodType.getSelectionModel().getSelectedItem() == null) {
            lblBloodType.setText("Please choose your blood type.");
            return;
        }
        if (comboRH.getSelectionModel().getSelectedItem() == null) {
            lblRH.setText("Please choose your RH.");
            return;
        }
        if (comboCounty.getSelectionModel().getSelectedItem() == null) {
            lblCounty.setText("Please choose your county.");
            return;
        }
        if (txtCNP.getText().isEmpty()) {
            lblCNP.setText("Please enter your CNP.");
            return;
        }
        Boolean RH = transformRHIntoBoolean();
        County userCounty = mapCountyNameToCounty(counties);
        BloodType bloodType = new BloodType(comboBloodType.getValue(), RH);
        Donor donor = new Donor(txtFirstName.getText(), txtLastName.getText(), txtPhoneNumber.getText(), dateDOB.getValue(), txtEmail.getText(), passPassword.getText(), bloodType, userCounty, txtCNP.getText());
        if (!donor.verifyFirstName()) {
            lblFirstName.setText("First name must contain only letters.");
            return;
        }
        if (!donor.verifyLastName()) {
            lblLastName.setText("Last name must contain only letters.");
            return;
        }
        if (!donor.isEmailValid()) {
            lblEmail.setText("This email is not valid.");
            return;
        }
        if (!donor.verifyPhoneNumber()) {
            lblPhoneNumber.setText("Your phone number must have 10 digits.");
            return;
        }
        if (!donor.isEligibleToDonate()) {
            lblDOB.setText("You must be over 18 and under 65 to donate blood.");
            return;
        }
        if (!donor.verifyCNP(lblCNP)) {
            return;
        }
        if (donor.verifyDateOfBirth() != 0) {
            lblDOB.setText("The date of birth does not coincide with the one in the CNP.");
            return;
        }
        if (!database.isDonorInDatabaseSignUp(donor)) {
            database.insertDonorInDatabase(donor);
            currentDonor = donor;
            //change scene to next page
            try {
                changeScene(event, "/mainMenu.fxml");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            anchorAlert.setVisible(true);
        }

    }

    public void goToSignIn(ActionEvent event) throws IOException {
        changeScene(event, "/signIn.fxml");
    }

    /**
     * clearing the text of the error labels from the sign up form
     */
    public void clearErrorLabels() {
        lblFirstName.setText("");
        lblLastName.setText("");
        lblEmail.setText("");
        lblPassword.setText("");
        lblConfirmPassword.setText("");
        lblPhoneNumber.setText("");
        lblDOB.setText("");
        lblBloodType.setText("");
        lblRH.setText("");
        lblCounty.setText("");
        lblCNP.setText("");
    }

    /**
     * @return true if user's RH is positive, false if negative
     */
    public Boolean transformRHIntoBoolean() {
        return comboRH.getValue().equals("positive");
    }

    /**
     * @return the ArrayList<String> containing strictly the county names
     */
    public ArrayList<String> createArrayWithCountyNames() {
        ArrayList<String> countyNames = new ArrayList<>();
        for (County c : counties) {
            countyNames.add(c.getName());
        }
        return countyNames;
    }

    /**
     * @param counties - array containing all of Romania's counties
     * @return the County which the user chose when signing in - o sa incerc sa ma gandesc la o solutie mai ok pentru treaba asta, dar dunno acum
     */
    public County mapCountyNameToCounty(ArrayList<County> counties) {
        for (County c : counties) {
            if (comboCounty.getValue().equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    public void closeAlert() {
        anchorAlert.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboRH.setItems(RHOptions);
        comboBloodType.setItems(bloodTypeOptions);
        ArrayList<String> countyNames = createArrayWithCountyNames();
        ObservableList<String> countyOptions = FXCollections.observableArrayList(countyNames);
        comboCounty.setItems(countyOptions);

        btnSignIn.setOnMouseEntered(e -> btnSignIn.setStyle(HOVERED_BUTTON_STYLE_SIGN_IN));
        btnSignIn.setOnMouseExited(e -> btnSignIn.setStyle(IDLE_BUTTON_STYLE_SIGN_IN));

        btnRegister.setOnMouseEntered(e -> btnRegister.setStyle(HOVERED_BUTTON_STYLE_REGISTER));
        btnRegister.setOnMouseExited(e -> btnRegister.setStyle(IDLE_BUTTON_STYLE_REGISTER));
    }
}
