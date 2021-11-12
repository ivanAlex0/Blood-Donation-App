package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import user.Appointment;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class AppointmentsController extends ParentController {
    @FXML
    private TextField textHour;
    @FXML
    private DatePicker appointmentDate;
    @FXML
    private Button eightAM;
    @FXML
    private Button nineAM;
    @FXML
    private Button tenAM;
    @FXML
    private Button elevenAM;
    @FXML
    private Button twelvePM;
    @FXML
    private Button onePM;
    @FXML
    private Label dateLabel;

    @FXML
    private void set8am() {
        textHour.setText("8");
    }

    @FXML
    private void set9am() {
        textHour.setText("9");
    }

    @FXML
    private void set10am() {
        textHour.setText("10");
    }

    @FXML
    private void set11am() {
        textHour.setText("11");
    }

    @FXML
    private void set12pm() {
        textHour.setText("12");
    }

    @FXML
    private void set1pm() {
        textHour.setText("1");
    }


    @FXML
    private void initialize() {
        appointmentDate.setOnAction(e -> {
            String date = appointmentDate.getValue().toString();

            eightAM.setDisable(false);
            eightAM.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-radius: 5em;" +
                            "-fx-border-color: red"
            );
            nineAM.setDisable(false);
            nineAM.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-radius: 5em;" +
                            "-fx-border-color: red"
            );
            tenAM.setDisable(false);
            tenAM.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-radius: 5em;" +
                            "-fx-border-color: red"
            );
            elevenAM.setDisable(false);
            elevenAM.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-radius: 5em;" +
                            "-fx-border-color: red"
            );
            twelvePM.setDisable(false);
            twelvePM.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-radius: 5em;" +
                            "-fx-border-color: red"
            );
            onePM.setDisable(false);
            onePM.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-radius: 5em;" +
                            "-fx-border-color: red"
            );

            ArrayList<Timestamp> datesOccupiedOnDate = database.getOccupiedOnDate(date, bloodBanks.indexOf(currentBloodBank) + 1);
            for (Timestamp s : datesOccupiedOnDate) {
                String[] parts = s.toString().split(" ");
                if (parts[1].contains("08")) {
                    eightAM.setDisable(true);
                    eightAM.setStyle(
                            "-fx-background-color: #fc3903;" +
                                    "-fx-border-color: white;" +
                                    "-fx-border-radius: 5em;"
                    );
                }

                if (parts[1].contains("09")) {
                    nineAM.setDisable(true);
                    nineAM.setStyle(
                            "-fx-background-color: #fc3903;" +
                                    "-fx-border-color: white;" +
                                    "-fx-border-radius: 5em;"
                    );
                }

                if (parts[1].contains("10")) {
                    tenAM.setDisable(true);
                    tenAM.setStyle(
                            "-fx-background-color: #fc3903;" +
                                    "-fx-border-color: white;" +
                                    "-fx-border-radius: 5em;"
                    );
                }

                if (parts[1].contains("11")) {
                    elevenAM.setDisable(true);
                    elevenAM.setStyle(
                            "-fx-background-color: #fc3903;" +
                                    "-fx-border-color: white;" +
                                    "-fx-border-radius: 5em;"
                    );
                }

                if (parts[1].contains("12")) {
                    twelvePM.setDisable(true);
                    twelvePM.setStyle(
                            "-fx-background-color: #fc3903;" +
                                    "-fx-border-color: white;" +
                                    "-fx-border-radius: 5em;"
                    );
                }

                if (parts[1].contains("13")) {
                    onePM.setDisable(true);
                    onePM.setStyle(
                            "-fx-background-color: #fc3903;" +
                                    "-fx-border-color: white;" +
                                    "-fx-border-radius: 5em;"
                    );
                }
            }
        });
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            changeScene(event, "/bloodBankPicker.fxml");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    private void setAppointment(ActionEvent event) {
        if (!textHour.getText().isEmpty() && appointmentDate.getValue() != null) {
            dateLabel.setText("");
            if (appointmentDate.getValue().isBefore(LocalDate.now())) {
                dateLabel.setText("The date of your appointment must be in the future.");
                return;
            }
            if (!LocalDate.now().getMonth().equals(Month.NOVEMBER) && !LocalDate.now().getMonth().equals(Month.DECEMBER) ) {
                if (!Year.of(appointmentDate.getValue().getYear()).equals(Year.now())) {
                    dateLabel.setText("You can only make appointments for the current year.");
                    return;
                }
            } else {
                if (!Year.of(appointmentDate.getValue().getYear()).equals(Year.now()) && !Year.of(appointmentDate.getValue().getYear()).equals(Year.of(Integer.parseInt(String.valueOf(Year.now())) + 1))) {
                    dateLabel.setText("You can only make appointments for the current and next year.");
                    return;
                }
            }
            String[] parts = appointmentDate.getValue().toString().split("-");
            Timestamp timestamp = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:00:00", Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(textHour.getText())));

            for (Appointment appointment : currentDonor.getAppointments()) {
                String[] parts_app = appointment.getAppointmentDate().toString().split("-");
                boolean canDonate = true;
                if (parts[0].equals(parts_app[0])) {
                    if (Math.abs(Integer.parseInt(parts[1]) - Integer.parseInt(parts_app[1])) < 3) {
                        canDonate = false;
                    } else if (Math.abs(Integer.parseInt(parts[1]) - Integer.parseInt(parts_app[1])) == 3) {
                        if (Integer.parseInt(parts[1]) > Integer.parseInt(parts_app[1])) {
                            System.out.println(Integer.parseInt(parts[1]) + " " + Integer.parseInt(parts_app[1]));
                            System.out.println(parts[2].substring(0, 2) + " " + parts_app[2].substring(0, 2));
                            if (Integer.parseInt(parts[2].substring(0, 2)) < Integer.parseInt(parts_app[2].substring(0, 2))) {
                                canDonate = false;
                            }
                        } else {
                            if (Integer.parseInt(parts[2].substring(0, 2)) > Integer.parseInt(parts_app[2].substring(0, 2))) {
                                canDonate = false;
                            }
                        }
                    }
                }
                if (!canDonate) {
                    dateLabel.setText("You cannot donate twice in 3 months. Closest: " + appointment.getAppointmentDate());
                    return;
                }
            }
            currentTimeStamp = timestamp;
            try {
                changeScene(event, "/checkInFile.fxml");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            dateLabel.setText("You must first specify a date and an hour!");
        }
    }

}
