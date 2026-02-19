package notjippity.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;

/**
 * Contains JUnit tests for the DateTimeUtils class.
 */
public class DateTimeUtilsTest {

    /**
     * Tests that parseDateTime returns correct LocalDateTime for valid input.
     */
    @Test
    public void parseDateTime_validInput_returnsLocalDateTime() throws CmdFormatException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        LocalDateTime result = DateTimeUtils.parseDateTime("25/12/2026 1430", formatter, "Format: test");
        assertEquals(LocalDateTime.of(2026, 12, 25, 14, 30), result);
    }

    /**
     * Tests that parseDateTime throws CmdFormatException for invalid format.
     */
    @Test
    public void parseDateTime_invalidFormat_throwsCmdFormatException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        CmdFormatException exception = assertThrows(CmdFormatException.class, () ->
                DateTimeUtils.parseDateTime("invalid-date wdawdawd", formatter, "Format: test"));
        assertEquals("Follow the date format pls (Format: test)", exception.getMessage());
    }

    /**
     * Tests that parseDate returns correct LocalDate for valid input.
     */
    @Test
    public void parseDate_validInput_returnsLocalDate() throws CmdFormatException, MissingArgException {
        LocalDate result = DateTimeUtils.parseDate("--date 25/12/2026",
                "--date", "dd/MM/yyyy", "Format: test");
        assertEquals(LocalDate.of(2026, 12, 25), result);

        LocalDate resultArgUpperCase = DateTimeUtils.parseDate("--DATE 25/12/2026",
                "--date", "dd/MM/yyyy", "Format: test");
        assertEquals(LocalDate.of(2026, 12, 25), resultArgUpperCase);

        LocalDate resultFlagUpperCase = DateTimeUtils.parseDate("--DATE 25/12/2026",
                "--DATE", "dd/MM/yyyy", "Format: test");
        assertEquals(LocalDate.of(2026, 12, 25), resultFlagUpperCase);
    }

    /**
     * Tests that parseDate throws correct exceptions for various invalid inputs.
     */
    @Test
    public void parseDate_invalidInputs_throwsException() {
        // Null input
        assertThrows(MissingArgException.class, () ->
                DateTimeUtils.parseDate(null, "--date", "dd/MM/yyyy", "Format: test"));

        // Empty date value
        assertThrows(MissingArgException.class, () ->
                DateTimeUtils.parseDate("--date ", "--date", "dd/MM/yyyy", "Format: test"));

        // Invalid date format
        CmdFormatException exception = assertThrows(CmdFormatException.class, () ->
                DateTimeUtils.parseDate("--date invalid", "--date", "dd/MM/yyyy", "Format: test"));
        assertEquals("Sry bro can't understand that date format (dd/MM/yyyy)", exception.getMessage());
    }

    /**
     * Tests that formatDate returns correctly formatted date string.
     */
    @Test
    public void formatDate_validDate_returnsFormattedString() {
        LocalDate date = LocalDate.of(2026, 12, 25);
        String result = DateTimeUtils.formatDate(date, "dd/MM/yyyy");
        assertEquals("25/12/2026", result);
    }

    /**
     * Tests that formatDate works with different format patterns.
     */
    @Test
    public void formatDate_differentPatterns_returnsCorrectFormat() {
        LocalDate date = LocalDate.of(2026, 3, 5);
        assertEquals("05/03/2026", DateTimeUtils.formatDate(date, "dd/MM/yyyy"));
        assertEquals("2026-03-05", DateTimeUtils.formatDate(date, "yyyy-MM-dd"));
        assertEquals("05 Mar 2026", DateTimeUtils.formatDate(date, "dd MMM yyyy"));
    }

}
