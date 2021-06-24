package academy.mindswap.utils;

public class Utils {

    //PROPERTIES
    private static int randomChoice;

    //METHODS
    public static int random (int min, int max){
        randomChoice = (int) (Math.random() * (max - min + 1) - min);
        return randomChoice;
    }
}
