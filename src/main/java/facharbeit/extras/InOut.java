package facharbeit.extras;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Support class for console input and output of numeric values.<br>
 * <br>
 * 
 * Example for input:<br>
 * &nbsp;int age = InOut.readInt("Your age: ");<br>
 * <br>
 * 
 * Example for output:<br>
 * &nbsp;System.out.println("price: " + InOut.format2(prize) + "Euro");<br>
 * &nbsp;System.out.println("percent: " + InOut.formatN(percent, 1) + " %");
 */

public class InOut {

    /** Formats a double-value with 2 decimal digits. */
    public static String format2(double d) {
	return String.format("%.2f", d);
    }

    /** Formats a double-value with N decimal digits. */
    public static String formatN(double d, int N) {
	return String.format("%." + N + "f", d);
    }

    /** Reads a boolean-value from console. */
    public static boolean readBoolean(String prompt) {
	final String[] trueValues = { "1", "y", "t", "j", "w", "yes", "true", "ja", "wahr", "ok" };
	System.out.print(prompt);
	String input = readln().toLowerCase();
	for (int i = 0; i < trueValues.length; ++i)
	    if (trueValues[i].equals(input))
		return true;
	return false;
    }

    /** Reads a char-value from console. */
    public static char readChar(String prompt) {
	System.out.print(prompt);
	return readln().charAt(0);
    }

    /** Reads a double-value from console. */
    public static double readDouble(String prompt) {
	System.out.print(prompt);
	return Double.parseDouble(readln());
    }

    /** Reads a float-value from console. */
    public static float readFloat(String prompt) {
	System.out.print(prompt);
	return Float.parseFloat(readln());
    }

    /** Reads an int-value from console. */
    public static int readInt(String prompt) {
	System.out.print(prompt);
	return Integer.parseInt(readln());
    }

    /** Reads a long-value from console. */
    public static long readLong(String prompt) {
	System.out.print(prompt);
	return Long.parseLong(readln());
    }

    /** Reads a string-value from console. */
    public static String readString(String prompt) {
	System.out.print(prompt);
	return readln();
    }

    /**
     * Reads a string-value from console without prompt. For use at the end of a
     * console program.
     */
    public static String readln() {
	try {
	    return Input.readLine();
	} catch (Exception e) {
	    return "";
	}
    }

    private static BufferedReader Input;

    static {
	try {
	    Input = new BufferedReader(new InputStreamReader(System.in));
	} catch (Exception e) {
	    System.out.println("console input not possible.");
	}
    }
}
