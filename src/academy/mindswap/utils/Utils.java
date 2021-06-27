package academy.mindswap.utils;

public class Utils {

    //PROPERTIES
    private static int randomChoice;

    //METHODS

    /**
     * Random - generates a random integer within a certain range
     * @param min -> the lowest number it can generate
     * @param max -> the highest number it can generate
     * @return -> the number generated
     */
    public static int random (int min, int max){
        randomChoice = (int) (Math.random() * (max - min + 1) + min);
        return randomChoice;
    }
}
