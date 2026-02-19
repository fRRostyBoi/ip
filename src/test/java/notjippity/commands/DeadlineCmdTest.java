package notjippity.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.TaskTracker;

/**
 * Contains JUnit tests for the DeadlineCmd class.
 */
public class DeadlineCmdTest {

    private TaskTracker taskTracker;
    private DeadlineCmd deadlineCmd;

    /**
     * Sets up a new TaskTracker and DeadlineCmd before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
        deadlineCmd = new DeadlineCmd(taskTracker);
    }

    /**
     * Tests that execute throws MissingArgException for various missing argument scenarios.
     */
    @Test
    public void execute_missingArguments_throwsMissingArgException() {
        // Null argStr
        MissingArgException nullException = assertThrows(MissingArgException.class, () ->
                deadlineCmd.execute("deadline", null));
        assertTrue(nullException.getMessage().contains("what's this task called"));

        // Missing --by flag
        MissingArgException missingFlagException = assertThrows(MissingArgException.class, () ->
                deadlineCmd.execute("deadline", "Submit report"));
        assertTrue(missingFlagException.getMessage().contains("--by"));

        // Starts with --by flag (missing task name)
        MissingArgException startsWithException = assertThrows(MissingArgException.class, () ->
                deadlineCmd.execute("deadline", "--by 25/12/2026 1430"));
        assertTrue(startsWithException.getMessage().contains("what's this task called"));

        // Empty date value after --by
        MissingArgException emptyValueException = assertThrows(MissingArgException.class, () ->
                deadlineCmd.execute("deadline", "Submit report --by"));
        assertTrue(emptyValueException.getMessage().contains("--by"));
    }

    /**
     * Tests that execute throws CmdFormatException for invalid date format.
     */
    @Test
    public void execute_invalidDateFormat_throwsCmdFormatException() {
        CmdFormatException exception = assertThrows(CmdFormatException.class, () ->
                deadlineCmd.execute("deadline", "Submit report --by invalid-date-format-here"));
        assertTrue(exception.getMessage().contains("date format"));
    }

    /**
     * Tests that execute successfully creates a deadline with valid input.
     */
    @Test
    public void execute_validInput_addsDeadline() throws CmdFormatException, MissingArgException {
        CmdOutput output = deadlineCmd.execute("deadline", "Submit report --by 25/12/2026 1430");

        assertFalse(output.isError());
        assertEquals(1, taskTracker.getSize());
        assertTrue(output.getReply().get(0).contains("Submit report"));
        assertTrue(output.getReply().get(0).contains("25/12/2026 1430"));
    }

    /**
     * Tests that execute works with different casing --by flag.
     */
    @Test
    public void execute_diffCaseByFlag_addsDeadline() throws CmdFormatException, MissingArgException {
        CmdOutput output = deadlineCmd.execute("deadline", "Submit report --BY 25/12/2026 1430");
        CmdOutput output2 = deadlineCmd.execute("deadline", "Submit report --bY 25/12/2026 1430");

        assertEquals(2, taskTracker.getSize());
        assertFalse(output.isError());
        assertFalse(output2.isError());
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsDeadline() {
        assertEquals("deadline", deadlineCmd.getCmdName());
    }

}
