package notjippity.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.StorageException;

/**
 * Utility class for parsing task data from storage format.
 */
public class TaskDataParser {

    /**
     * Validates that dataParts array has the expected length.
     *
     * @param dataParts      The dataParts array to validate.
     * @param expectedLength The expected length.
     * @throws StorageException If the length doesn't match.
     */
    public static void validateDataPartsLength(String[] dataParts, int expectedLength) throws StorageException {
        if (dataParts.length < expectedLength) {
            throw new StorageException("Insufficient arguments; expected " + expectedLength
                    + " but found " + dataParts.length);
        } else if (dataParts.length > expectedLength) {
            throw new StorageException("Too many arguments; expected " + expectedLength
                    + " but found " + dataParts.length);
        }
    }

    /**
     * Parses and validates the name field.
     *
     * @param name The name string.
     * @return The validated name.
     * @throws StorageException If name is blank.
     */
    public static String parseName(String name) throws StorageException {
        if (name.isBlank()) {
            throw new StorageException("Invalid argument #1; expected Task name but found empty string");
        }
        return name;
    }

    /**
     * Parses the status string into a boolean.
     *
     * @param statusStr The status string ("Y" or "N").
     * @return True if completed, false otherwise.
     * @throws StorageException If the status string is invalid.
     */
    public static boolean parseCompletionStatus(String statusStr) throws StorageException {
        if (statusStr.equals("Y")) {
            return true;
        }

        if (statusStr.equals("N")) {
            return false;
        }

        throw new StorageException("Invalid argument #3; expected Y/N but found " + statusStr);
    }

    /**
     * Parses a datetime string into LocalDateTime.
     *
     * @param dateTimeStr The datetime string to parse.
     * @param formatter   The formatter to use.
     * @param argIndex    The argument index for error messages.
     * @return The parsed LocalDateTime.
     * @throws StorageException If parsing fails.
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, DateTimeFormatter formatter, int argIndex)
            throws StorageException {
        try {
            return UserInputParser.parseDateTime(dateTimeStr, formatter);
        } catch (InvalidArgException exception) {
            throw new StorageException("Invalid argument #" + argIndex
                    + "; expected DateTime but found empty string");
        } catch (DateTimeParseException exception) {
            throw new StorageException("Invalid argument #" + argIndex
                    + "; expected format " + formatter.toString() + " but found " + dateTimeStr);
        }
    }
}

