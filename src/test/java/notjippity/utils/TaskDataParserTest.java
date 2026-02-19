package notjippity.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.StorageException;

/**
 * Contains JUnit tests for the TaskDataParser class.
 */
public class TaskDataParserTest {

    /**
     * Tests that validateDataPartsLength does not throw any exceptions for valid length.
     */
    @Test
    public void validateDataPartsLength_validLength_noException() throws StorageException {
        TaskDataParser.validateDataPartsLength(new String[]{"T", "Task name", "N"}, 3);
        TaskDataParser.validateDataPartsLength(new String[]{"D", "Task name", "N", "ByDate"}, 4);
        TaskDataParser.validateDataPartsLength(new String[]{"E", "Task name", "N", "FromDate", "ToDate"}, 5);
    }

    /**
     * Tests that validateDataPartsLength does not throw any exceptions for valid length.
     */
    @Test
    public void validateDataPartsLength_invalidLength_throwsStorageException() {
        String[] dataPartsThree = {"E", "Task name", "N"};
        String[] dataPartsFour = {"T", "Task name", "N", "Extra part"};

        StorageException exceptionMore = assertThrows(StorageException.class, () ->
                TaskDataParser.validateDataPartsLength(dataPartsFour, 3));
        assertTrue(exceptionMore.getMessage().contains("Too many arguments"));
        assertTrue(exceptionMore.getMessage().contains("expected 3"));
        assertTrue(exceptionMore.getMessage().contains("found 4"));

        StorageException exceptionLess = assertThrows(StorageException.class, () ->
                TaskDataParser.validateDataPartsLength(dataPartsThree, 4));
        assertTrue(exceptionLess.getMessage().contains("Insufficient arguments"));
        assertTrue(exceptionLess.getMessage().contains("expected 4"));
        assertTrue(exceptionLess.getMessage().contains("found 3"));
    }

    /**
     * Tests that parseName returns the name for valid input.
     */
    @Test
    public void parseName_validName_returnsName() throws StorageException {
        String result = TaskDataParser.parseName("Task name");
        assertEquals("Task name", result);
    }

    /**
     * Tests that parseName throws StorageException for blank name.
     */
    @Test
    public void parseName_invalidName_throwsStorageException() {
        StorageException exception = assertThrows(StorageException.class, () ->
                TaskDataParser.parseName("   "));
        assertTrue(exception.getMessage().contains("empty string"));
    }

    /**
     * Tests that parseCompletionStatus returns true for "Y" and false for "N".
     */
    @Test
    public void parseCompletionStatus_validStatus_returnsBoolean() throws StorageException {
        assertTrue(TaskDataParser.parseCompletionStatus("Y"));
        assertFalse(TaskDataParser.parseCompletionStatus("N"));
    }

    /**
     * Tests that parseCompletionStatus throws StorageException for invalid status.
     */
    @Test
    public void parseCompletionStatus_invalidStatus_throwsStorageException() {
        StorageException exception = assertThrows(StorageException.class, () ->
                TaskDataParser.parseCompletionStatus("X"));
        assertTrue(exception.getMessage().contains("expected Y/N"));
        assertTrue(exception.getMessage().contains("found X"));
    }

    /**
     * Tests that parseDateTime returns correct LocalDateTime for valid input.
     */
    @Test
    public void parseDateTime_validDateTime_returnsLocalDateTime() throws StorageException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        LocalDateTime result = TaskDataParser.parseDateTime("25/12/2026 1430", formatter, 4);
        assertEquals(LocalDateTime.of(2026, 12, 25, 14, 30), result);
    }

    /**
     * Tests that parseDateTime throws StorageException for empty datetime or invalid format
     */
    @Test
    public void parseDateTime_invalidDateTime_throwsStorageException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        StorageException errorEmpty = assertThrows(StorageException.class, () ->
                TaskDataParser.parseDateTime("   ", formatter, 4));
        assertTrue(errorEmpty.getMessage().contains("argument #4"));
        assertTrue(errorEmpty.getMessage().contains("empty string"));

        StorageException errorInvalid = assertThrows(StorageException.class, () ->
                TaskDataParser.parseDateTime("invalid-date wasdwasd", formatter, 4));
        assertTrue(errorInvalid.getMessage().contains("argument #4"));
    }

}
