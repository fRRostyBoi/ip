package notjippity.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import notjippity.exceptions.InvalidArgException;

/**
 * Contains helper functions for parsing user inputs.
 */
public class Parser {

    /**
     * Returns only the command portion of the user input.
     *
     * @param input The full user input.
     * @return The command, or null if an empty input is provided.
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
     * Returns only the argument string potion of the user input.
     *
     * @param input The full user input.
     * @return The string of arguments, or null if an empty string of arguments is provided.
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

    /**
     * Parses a LocalDate from a string. Returns an error message.
     *
     * @param dateTimeStr The string to parse from.
     * @param formatter   The DateTimeFormatter object with the desired format.
     * @return The LocalDate object formed from the string.
     * @throws InvalidArgException    If the string is blank.
     * @throws DateTimeParseException If a parsing error occurs.
     */
    public static LocalDate parseDate(String dateTimeStr, DateTimeFormatter formatter)
            throws InvalidArgException, DateTimeParseException {
        LocalDate dateTime;

        if (dateTimeStr.trim().isEmpty()) {
            throw new InvalidArgException("Expected LocalDateTime but found empty string");
        }

        dateTime = LocalDate.parse(dateTimeStr, formatter);

        return dateTime;
    }

    /**
     * Parses a LocalDateTime from a string. Returns an error message.
     *
     * @param dateTimeStr The string to parse from.
     * @param formatter   The DateTimeFormatter object with the desired format.
     * @return The LocalDateTime object formed from the string.
     * @throws InvalidArgException    If the string is blank.
     * @throws DateTimeParseException If a parsing error occurs.
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, DateTimeFormatter formatter)
            throws InvalidArgException, DateTimeParseException {
        LocalDateTime dateTime;

        if (dateTimeStr.trim().isEmpty()) {
            throw new InvalidArgException("Expected LocalDateTime but found empty string");
        }

        dateTime = LocalDateTime.parse(dateTimeStr, formatter);

        return dateTime;
    }

}
