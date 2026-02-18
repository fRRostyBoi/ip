package notjippity.utils;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;

/**
 * Utility class for common validation operations.
 */
public class CmdValidator {

    /**
     * Validates that the value is not null.
     *
     * @param value    The value to check.
     * @param errorMsg The error message to throw.
     * @throws MissingArgException If the value is null.
     */
    public static void validateNotNull(Object value, String errorMsg) throws MissingArgException {
        if (value == null) {
            throw new MissingArgException(errorMsg);
        }
    }

    /**
     * Validates that the string is not empty.
     *
     * @param value    The value to check.
     * @param errorMsg The error message to throw.
     * @throws MissingArgException If the value is null or empty.
     */
    public static void validateNotEmpty(String value, String errorMsg) throws MissingArgException {
        if (value == null || value.isEmpty()) {
            throw new MissingArgException(errorMsg);
        }
    }

    /**
     * Validates that the argument string contains the specified flag.
     *
     * @param argStr   The argument string.
     * @param flag     The flag to check for.
     * @param errorMsg The error message to throw.
     * @throws MissingArgException If the argument doesn't contain the flag.
     */
    public static void validateContains(String argStr, String flag, String errorMsg)
            throws MissingArgException {
        if (!argStr.toLowerCase().contains(flag.toLowerCase())) {
            throw new MissingArgException(errorMsg);
        }
    }

    /**
     * Validates that flag1 appears before flag2 in the argument string.
     *
     * @param argStr   The argument string.
     * @param flag1    The first flag.
     * @param flag2    The second flag.
     * @param errorMsg The error message to throw.
     * @throws CmdFormatException If flag1 appears after flag2.
     */
    public static void validateOrder(String argStr, String flag1, String flag2, String errorMsg)
            throws CmdFormatException {
        String argStrLow = argStr.toLowerCase();
        int index1 = argStrLow.indexOf(flag1.toLowerCase());
        int index2 = argStrLow.indexOf(flag2.toLowerCase());

        if (index1 > index2) {
            throw new CmdFormatException(errorMsg);
        }
    }

    /**
     * Validates that the argument string doesn't start with the specified flag.
     *
     * @param argStr   The argument string.
     * @param flag     The flag to check for.
     * @param errorMsg The error message to throw.
     * @throws MissingArgException If the argument starts with the flag.
     */
    public static void validateNotStartsWith(String argStr, String flag, String errorMsg)
            throws MissingArgException {
        if (argStr.toLowerCase().startsWith(flag.toLowerCase())) {
            throw new MissingArgException(errorMsg);
        }
    }
}

