package user;

public class BloodType {

    private String name;
    private Boolean RH;

    public BloodType(String name, Boolean RH) {
        this.name = name;
        this.RH = RH;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRH() {
        return RH;
    }

    public void setRH(Boolean RH) {
        this.RH = RH;
    }
}
