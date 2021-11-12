package user;

public class BloodBank {
    private String name;
    private String phoneNumber;
    private County county;

    public BloodBank(String name, String phoneNumber, County county) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.county = county;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }
}
