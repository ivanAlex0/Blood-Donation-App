package user;

/**
 * @returns true if the potential donor is able to donate
 */
public class Verifier {

    public Boolean checkDates(Boolean drankAlcohol, Boolean treatment, Boolean tattoo, Boolean coronaSymptoms, Boolean chronicDiseases){
        if(drankAlcohol.equals(true)){
            System.out.println("Sorry, you can't donate!");
            return false;
        }
        else{
            if(treatment.equals(true)){
                System.out.println("Sorry, you can't donate!");
                return false;
            }
            else{
                if(tattoo.equals(true)){
                    System.out.println("Sorry, you can't donate!");
                    return false;
                }
                else{
                    if(coronaSymptoms.equals(true)){
                        System.out.println("Sorry, you can't donate!");
                        return false;
                    }
                    else{
                        if(chronicDiseases.equals(true)){
                            System.out.println("Sorry, you can't donate!");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
