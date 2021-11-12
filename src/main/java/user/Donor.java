package user;

import java.time.LocalDate;

public class Donor extends User {

    private String email;
    private String password;
    private int weight;

    public Donor(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public Donor(String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth, String email, String password, BloodType bloodType, County county, String CNP) {
        super(firstName, lastName, phoneNumber, dateOfBirth, bloodType, county, CNP);
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailValid() {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return this.email.matches(regex);
    }
}
