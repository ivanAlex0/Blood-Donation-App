package user;

public class County {
    private String name;
    private int numberOfBloodBanks;

    public County(String name, int numberOfBloodBanks) {
        this.name = name;
        this.numberOfBloodBanks = numberOfBloodBanks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfBloodBanks() {
        return numberOfBloodBanks;
    }

    public void setNumberOfBloodBanks(int numberOfBloodBanks) {
        this.numberOfBloodBanks = numberOfBloodBanks;
    }
}
