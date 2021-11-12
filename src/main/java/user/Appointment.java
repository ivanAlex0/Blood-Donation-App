package user;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private Timestamp appointmentDate;
    private Donor donor;
    private BloodBank bloodBank;
    private Hospital hospital;

    public Appointment(Timestamp appointmentDate, Donor donor, BloodBank bloodBank, Hospital hospital) {
        this.appointmentDate = appointmentDate;
        this.donor = donor;
        this.bloodBank = bloodBank;
        this.hospital = hospital;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Timestamp appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public BloodBank getBloodBank() {
        return bloodBank;
    }

    public void setBloodBank(BloodBank bloodBank) {
        this.bloodBank = bloodBank;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
