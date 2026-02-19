package notjippity.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.InvalidArgException;

/**
 * Contains JUnit tests for the UserInputParser class.
 */
public class UserInputParserTest {

    /**
     * Tests the getCommand method with various input formats.
     */
    @Test
    public void getCommand_variousInputs_correctCmdExtraction() {
        assertEquals("cmd", UserInputParser.getCommand("cmd"));
        assertEquals("cmd", UserInputParser.getCommand("   cmd"));
        assertEquals("cmd", UserInputParser.getCommand("cmd   "));
        assertEquals("cmd", UserInputParser.getCommand("   cmd   "));
        assertEquals("c", UserInputParser.getCommand("  c  m  d"));
        assertEquals("c", UserInputParser.getCommand("     c"));

        assertNull(UserInputParser.getCommand(""));
        assertNull(UserInputParser.getCommand("   "));
        assertNull(UserInputParser.getCommand(null));
    }

    /**
     * Tests the getArgString method with various input formats.
     */
    @Test
    public void getArgString_variousInputs_correctArgExtraction() {
        assertEquals("arg1 arg2", UserInputParser.getArgString("cmd arg1 arg2"));
        assertEquals("arg1 arg2", UserInputParser.getArgString("   cmd arg1 arg2"));
        assertEquals("arg1 arg2", UserInputParser.getArgString("   cmd arg1 arg2    "));
        assertEquals("arg1 arg2", UserInputParser.getArgString("   cmd arg1 arg2    "));
        assertEquals("arg1 arg2", UserInputParser.getArgString("   cmd     arg1 arg2    "));
        assertEquals("arg1   arg2", UserInputParser.getArgString("   cmd arg1   arg2    "));
        assertEquals("arg1   arg2", UserInputParser.getArgString("   cmd    arg1   arg2    "));

        assertNull(UserInputParser.getArgString("cmd"));
        assertNull(UserInputParser.getArgString("   cmd   "));

        assertNull(UserInputParser.getArgString(""));
        assertNull(UserInputParser.getArgString("   "));
        assertNull(UserInputParser.getArgString(null));
    }

    /**
     * Tests the parseDate method with valid date string.
     */
    @Test
    public void parseDate_validDateString_returnsLocalDate() throws InvalidArgException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate result = UserInputParser.parseDate("25/12/2026", formatter);
        assertEquals(LocalDate.of(2026, 12, 25), result);
    }

    /**
     * Tests the parseDate method throws InvalidArgException for blank string.
     */
    @Test
    public void parseDate_blankString_throwsInvalidArgException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            UserInputParser.parseDate("   ", formatter);
            fail("Expected InvalidArgException");
        } catch (InvalidArgException e) {
            assertEquals("Expected LocalDate but found empty string", e.getMessage());
        }
    }

    /**
     * Tests the parseDate method throws DateTimeParseException for invalid format.
     */
    @Test
    public void parseDate_invalidFormat_throwsDateTimeParseException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            UserInputParser.parseDate("invalid-date", formatter);
            fail("Expected DateTimeParseException");
        } catch (InvalidArgException e) {
            fail("Expected DateTimeParseException but found InvalidArgException");
        } catch (DateTimeParseException e) {
            // Expected
        }
    }

    /**
     * Tests the parseDateTime method with valid datetime string.
     */
    @Test
    public void parseDateTime_validDateTimeString_returnsLocalDateTime() throws InvalidArgException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        LocalDateTime result = UserInputParser.parseDateTime("25/12/2026 1430", formatter);
        assertEquals(LocalDateTime.of(2026, 12, 25, 14, 30), result);
    }

    /**
     * Tests the parseDateTime method throws appropriate exceptions for invalid inputs.
     */
    @Test
    public void parseDateTime_invalidInputs_throwsException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");

        // Blank string
        try {
            UserInputParser.parseDateTime("   ", formatter);
            fail("Expected InvalidArgException");
        } catch (InvalidArgException exception) {
            assertEquals("Expected LocalDateTime but found empty string", exception.getMessage());
        }

        // Invalid format
        try {
            UserInputParser.parseDateTime("invalid-datetime wdw awdawd efe", formatter);
            fail("Expected DateTimeParseException");
        } catch (InvalidArgException exception) {
            fail("Expected DateTimeParseException but found InvalidArgException");
        } catch (DateTimeParseException exception) {
            // Expected
        }
    }

}
