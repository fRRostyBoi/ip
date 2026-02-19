package notjippity.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;

/**
 * Contains JUnit tests for the CmdValidator class.
 */
public class CmdValidatorTest {

    /**
     * Tests that validateNotNull runs normally for non-null value.
     */
    @Test
    public void validateNotNull_nonNullValue_noException() {
        assertDoesNotThrow(() -> CmdValidator.validateNotNull("value", "Error message"));
    }

    /**
     * Tests that validateNotNull throws MissingArgException for null value.
     */
    @Test
    public void validateNotNull_nullValue_throwsMissingArgException() {
        MissingArgException exception = assertThrows(MissingArgException.class, () ->
                CmdValidator.validateNotNull(null, "Custom error"));
        assertEquals("Custom error", exception.getMessage());
    }

    /**
     * Tests that validateNotEmpty runs normally for non-empty string.
     */
    @Test
    public void validateNotEmpty_nonEmptyString_noException() {
        assertDoesNotThrow(() -> CmdValidator.validateNotEmpty("value", "Error message"));
    }

    /**
     * Tests that validateNotEmpty throws MissingArgException for null string.
     */
    @Test
    public void validateNotEmpty_emptyOrNullString_throwsMissingArgException() {
        MissingArgException nullError = assertThrows(MissingArgException.class, () ->
                CmdValidator.validateNotEmpty(null, "Null error"));
        assertEquals("Null error", nullError.getMessage());

        MissingArgException emptyError = assertThrows(MissingArgException.class, () ->
                CmdValidator.validateNotEmpty("", "Empty error"));
        assertEquals("Empty error", emptyError.getMessage());
    }

    /**
     * Tests that validateContains runs normally when string contains flag, case-insensitive.
     */
    @Test
    public void validateContains_containsFlag_noException() {
        assertDoesNotThrow(() -> CmdValidator.validateContains("test --by tomorrow", "--by", "Error"));

        assertDoesNotThrow(() -> CmdValidator.validateContains("test --BY tomorrow", "--by", "Error"));
        assertDoesNotThrow(() -> CmdValidator.validateContains("test --by tomorrow", "--BY", "Error"));
    }

    /**
     * Tests that validateContains throws MissingArgException when flag is missing.
     */
    @Test
    public void validateContains_missingFlag_throwsMissingArgException() {
        MissingArgException exception = assertThrows(MissingArgException.class, () ->
                CmdValidator.validateContains("test tomorrow", "--by", "Missing flag"));
        assertEquals("Missing flag", exception.getMessage());
    }

    /**
     * Tests that validateOrder does not throw when flags are in correct order, case-insensitive
     */
    @Test
    public void validateOrder_correctOrder_noException() {
        assertDoesNotThrow(() -> CmdValidator.validateOrder("task --from 9am --to 5pm", "--from", "--to", "Error"));

        assertDoesNotThrow(() -> CmdValidator.validateOrder("task --FROM 9am --TO 5pm", "--from", "--to", "Error"));
        assertDoesNotThrow(() -> CmdValidator.validateOrder("task --FROM 9am --To 5pm", "--from", "--TO", "Error"));
        assertDoesNotThrow(() -> CmdValidator.validateOrder("task --fRoM 9am --TO 5pm", "--FROM", "--to", "Error"));
        assertDoesNotThrow(() -> CmdValidator.validateOrder("task --FroM 9am --to 5pm", "--FROM", "--TO", "Error"));
    }

    /**
     * Tests that validateOrder throws CmdFormatException when flags are in wrong order.
     */
    @Test
    public void validateOrder_wrongOrder_throwsCmdFormatException() {
        CmdFormatException exception = assertThrows(CmdFormatException.class, () ->
                CmdValidator.validateOrder("task --to 5pm --from 9am", "--from", "--to", "Wrong order"));
        assertEquals("Wrong order", exception.getMessage());
    }

    /**
     * Tests that validateNotStartsWith handles various inputs correctly, case-insensitive.
     */
    @Test
    public void validateNotStartsWith_variousInputs_noException() {
        assertDoesNotThrow(() -> CmdValidator.validateNotStartsWith("task name --by tomorrow", "--by", "Error"));

        assertThrows(MissingArgException.class, () ->
                CmdValidator.validateNotStartsWith("--BY tomorrow", "--by", "Error"));

        MissingArgException exception = assertThrows(MissingArgException.class, () ->
                CmdValidator.validateNotStartsWith("--by tomorrow", "--by", "Starts with flag"));
        assertEquals("Starts with flag", exception.getMessage());
    }

}
