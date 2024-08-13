package backend;

public class Conversiones {

    public static String intToBin(int entero) {

        String bin = Integer.toString(entero, 2);

        while (bin.length() < 16) {
            bin = "0" + bin;
        }

        return bin;
    }

    public static String intToHex(int entero) {

        String hex = Integer.toString(entero, 16);

        while (hex.length() < 4) {
            hex = "0" + hex;
        }

        return hex;
    }

    public static int binToInt(String binario) {
        return Integer.parseInt(binario, 2);
    }

    public static int hexToInt(String hexadecimal) {
        return Integer.parseInt(hexadecimal, 16);
    }

}
