package notjippity.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;

/**
 * Utility class for date and time parsing and formatting operations.
 */
public class DateTimeUtils {

    /**
     * Parses a date string using the specified format.
     *
     * @param dateStr   The date string to parse.
     * @param formatter The DateTimeFormatter to use.
     * @param formatCmd The format command to show in error messages.
     * @return The parsed LocalDateTime object.
     * @throws CmdFormatException If the date string cannot be parsed.
     */
    public static LocalDateTime parseDateTime(String dateStr, DateTimeFormatter formatter, String formatCmd)
            throws CmdFormatException {
        try {
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException exception) {
            throw new CmdFormatException("Follow the date format pls (" + formatCmd + ")");
        }
    }

    /**
     * Parses a date from an argument string that contains a flag prefix.
     *
     * @param argStr    The argument string.
     * @param flag      The flag prefix to remove.
     * @param format    The date format pattern.
     * @param formatCmd The format command to show in error messages.
     * @return The parsed LocalDate object.
     * @throws CmdFormatException  If the date string cannot be parsed.
     * @throws MissingArgException If the date value is missing.
     */
    public static LocalDate parseDate(String argStr, String flag, String format, String formatCmd)
            throws CmdFormatException, MissingArgException {
        CmdValidator.validateNotNull(argStr, "On which date? (" + formatCmd + ")");

        String dateStr = argStr.replaceFirst(flag, "").trim();
        CmdValidator.validateNotEmpty(dateStr, "On which date? (" + formatCmd + ")");

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException exception) {
            throw new CmdFormatException("Sry bro can't understand that date format (" + format + ")");
        }
    }

    /**
     * Formats a LocalDate using the specified format pattern.
     *
     * @param date   The LocalDate to format.
     * @param format The format pattern.
     * @return The formatted date string.
     */
    public static String formatDate(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }
}

