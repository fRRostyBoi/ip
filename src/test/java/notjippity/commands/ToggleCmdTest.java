package notjippity.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.TaskTracker;
import notjippity.tasks.ToDo;

/**
 * Contains JUnit tests for the ToggleCmd class.
 */
public class ToggleCmdTest {

    private TaskTracker taskTracker;
    private ToggleCmd toggleCmd;

    /**
     * Sets up a TaskTracker with test tasks before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
        taskTracker.addTask(new ToDo("Task 1"));
        taskTracker.addTask(new ToDo("Task 2"));
        toggleCmd = new ToggleCmd(taskTracker);
    }

    /**
     * Tests that execute throws appropriate exceptions for various invalid inputs.
     */
    @Test
    public void execute_invalidInputs_throwsException() {
        // Null argStr
        MissingArgException nullException = assertThrows(MissingArgException.class, () ->
                toggleCmd.execute("toggle", null));
        assertTrue(nullException.getMessage().contains("Which one"));

        // Non-integer input
        CmdFormatException formatException = assertThrows(CmdFormatException.class, () ->
                toggleCmd.execute("toggle", "abc"));
        assertTrue(formatException.getMessage().contains("index"));

        // Out-of-bounds index
        InvalidArgException oobException = assertThrows(InvalidArgException.class, () ->
                toggleCmd.execute("toggle", "99"));
        assertTrue(oobException.getMessage().contains("don't have task"));

        // Negative index
        InvalidArgException negException = assertThrows(InvalidArgException.class, () ->
                toggleCmd.execute("toggle", "-1"));
        assertTrue(negException.getMessage().contains("don't have task"));
    }

    /**
     * Tests that execute toggles the completion status correctly.
     */
    @Test
    public void execute_variousCompletionStates_togglesCorrectly()
            throws CmdFormatException, MissingArgException, InvalidArgException {
        // Incomplete to complete
        assertFalse(taskTracker.getTask(0).isCompleted());
        CmdOutput output1 = toggleCmd.execute("toggle", "1");
        assertFalse(output1.isError());
        assertTrue(taskTracker.getTask(0).isCompleted());
        assertTrue(output1.getReply().get(0).contains("[X]"));

        // Complete to incomplete
        CmdOutput output2 = toggleCmd.execute("toggle", "1");
        assertFalse(output2.isError());
        assertFalse(taskTracker.getTask(0).isCompleted());
        assertTrue(output2.getReply().get(0).contains("[  ]"));
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsToggle() {
        assertEquals("toggle", toggleCmd.getCmdName());
    }

}
