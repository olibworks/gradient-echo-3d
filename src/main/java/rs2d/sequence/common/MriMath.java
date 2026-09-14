package rs2d.sequence.common;

public class MriMath {

    /**
     * Ceil a number to a given decimal places
     *
     * @param numberToBeRounded : Double number
     * @param order             : Digits kept after the decimal point
     */
    public static double ceilToSubDecimal(double numberToBeRounded, double order) {
        return Math.ceil(numberToBeRounded * Math.pow(10, order)) / Math.pow(10, order);
    }

    /**
     * floor a number to a given decimal places
     *
     * @param numberToBeRounded : Double number
     * @param order             : Digits kept after the decimal point
     */
    public static double floorToSubDecimal(double numberToBeRounded, double order) {
        return Math.floor(numberToBeRounded * Math.pow(10, order)) / Math.pow(10, order);
    }

    public static double roundToDecimal(double numberToBeRounded, double order) {
        return Math.round(numberToBeRounded * Math.pow(10, order)) / Math.pow(10, order);
    }

    public static int floorEven(double value) {
        return (int) Math.floor(Math.round(value) / 2.0) * 2;
    }

    /**
     * Find the next inferior integer which can divide the dividend : dividend /
     * -divisor- = integer
     *
     * @param divisor  dividend / DIVISOR = integer
     * @param dividend DIVIDEND / divisor = integer
     * @return Next inferior integer which is a multiple of the dividend
     */
    public static int getInferiorDivisorToGetModulusZero(int divisor, int dividend) {
        boolean exit = true;
        int div;
        int new_divisor;
        do {
            div = (int) Math.ceil(dividend / ((double) divisor));
            new_divisor = (int) Math.floor(dividend / ((double) div));
            if (dividend % new_divisor == 0) {
                exit = false;
            } else {
                divisor = new_divisor;
            }
        } while (exit);
        return new_divisor;
    }

}