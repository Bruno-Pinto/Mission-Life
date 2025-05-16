package utils;

public class MathUtils {

    public static double roundToDecimal(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }

    public static double floorToDecimal(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.floor(value * scale) / scale;
    }

    public static double ceilToDecimal(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.ceil(value * scale) / scale;
    }
}
