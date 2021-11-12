package user;

public class CheckInFile{
    private Boolean drankAlcohol;
    private Boolean treatment;
    private Boolean tattoo;
    private Boolean rejectedDonation;
    private Boolean coronaSymptoms;
    private Boolean chronicDiseases;

    public CheckInFile(Boolean drankAlcohol, Boolean treatment, Boolean tattoo, Boolean rejectedDonation, Boolean coronaSymptoms, Boolean chronicDiseases) {
        this.drankAlcohol = drankAlcohol;
        this.treatment = treatment;
        this.tattoo = tattoo;
        this.rejectedDonation = rejectedDonation;
        this.coronaSymptoms = coronaSymptoms;
        this.chronicDiseases = chronicDiseases;
    }

    public Boolean getDrankAlcohol() {
        return drankAlcohol;
    }

    public void setDrankAlcohol(Boolean drankAlcohol) {
        this.drankAlcohol = drankAlcohol;
    }

    public Boolean getTreatment() {
        return treatment;
    }

    public void setTreatment(Boolean treatment) {
        this.treatment = treatment;
    }

    public Boolean getTattoo() {
        return tattoo;
    }

    public void setTattoo(Boolean tatto) {
        this.tattoo = tatto;
    }

    public Boolean getRejectedDonation() {
        return rejectedDonation;
    }

    public void setRejectedDonation(Boolean rejectedDonation) {
        this.rejectedDonation = rejectedDonation;
    }

    public Boolean getCoronaSymptoms() {
        return coronaSymptoms;
    }

    public void setCoronaSymptoms(Boolean coronaSyptoms) {
        this.coronaSymptoms = coronaSyptoms;
    }

    public Boolean getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(Boolean chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }
}
