package edu.cmu.cs.cs214.hw5.plugins.data.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for data plugin usage.
 */
public class Utility {
    /**
     * Check if a string is a valid URL or not.
     * @param input input string
     * @return true for valid URL and false for not valid
     */
    public static boolean isURL(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Parse a number string into integer.
     * 100.0 => 100; 122.23 => 122; null => -1; "" => -1.
     * @param input input number string
     * @return integer
     */
    public static int strToInt(String input) {
        if (input == null) {
            return -1;
        }
        if (input.equals("")) {
            return -1;
        }
        if (input.contains(".")) {
            Pattern p = Pattern.compile("(\\d+)[\\.]{1}(\\d+)");
            Matcher m = p.matcher(input);
            if (m.matches()) {
                return Integer.parseInt(m.group(1));
            }
        }
        return Integer.parseInt(input);
    }
}
