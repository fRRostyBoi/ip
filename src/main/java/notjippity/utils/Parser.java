package notjippity.utils;

/**
 * Contains helper functions for parsing user inputs
 */
public class Parser {

    /**
     * Returns only the command of the user input
     * @param input The full user input
     * @return The command, or null if an empty input is provided
     */
    public static String getCommand(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if (input.isEmpty()) {
            return null;
        }
        return input.split(" ")[0];
    }

    /**
     * Returns only the string of argument of the user input
     * @param input The full user input
     * @return The string of arguments, or null if an empty string of arguments was provided
     */
    public static String getArgString(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if (input.isEmpty()) {
            return null;
        }
        String argString = input.replaceFirst(getCommand(input), "").trim();
        if (argString.isEmpty()) {
            return null;
        }
        return argString;
    }

}
