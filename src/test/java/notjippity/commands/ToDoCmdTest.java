package notjippity.commands;

import static notjippity.commands.ToDoCmd.FORMAT_CMD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.MissingArgException;
import notjippity.tasks.TaskTracker;

/**
 * Contains JUnit tests for the ToDoCmd class.
 */
public class ToDoCmdTest {

    /**
     * Tests the execute method with valid argument inputs.
     * Verifies that no exceptions are thrown for valid inputs.
     */
    @Test
    public void execute_validInputs_noException() {
        TaskTracker taskTracker = new TaskTracker();

        try {
            new ToDoCmd(taskTracker).execute("todo", "test");
            new ToDoCmd(taskTracker).execute("todo", "test one two three");
        } catch (MissingArgException exception) {
            fail();
        }
    }

    /**
     * Tests the execute method with various argument inputs.
     * Verifies that MissingArgException is thrown for null, empty, and whitespace-only inputs.
     * Verifies that valid inputs are accepted without throwing exceptions.
     */
    @Test
    public void execute_invalidInputs_throwsRespectiveException() {
        TaskTracker taskTracker = new TaskTracker();

        // Null argStr
        try {
            new ToDoCmd(taskTracker).execute("todo", null);
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }

        // Empty argStr
        try {
            new ToDoCmd(taskTracker).execute("todo", "");
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }

        // Whitespace argStr
        try {
            new ToDoCmd(taskTracker).execute("todo", "         ");
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }

        // Breakline argStr
        try {
            new ToDoCmd(taskTracker).execute("todo", "\n");
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }

        // Whitespace-into-breakline argStr
        try {
            new ToDoCmd(taskTracker).execute("todo", "     \n");
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }
    }

}
