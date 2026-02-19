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
 * Contains JUnit tests for the DoneCmd class.
 */
public class DoneCmdTest {

    private TaskTracker taskTracker;
    private DoneCmd doneCmd;

    /**
     * Sets up a TaskTracker with test tasks before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
        taskTracker.addTask(new ToDo("Task 1"));
        taskTracker.addTask(new ToDo("Task 2"));
        doneCmd = new DoneCmd(taskTracker);
    }

    /**
     * Tests that execute throws appropriate exceptions for various invalid inputs.
     */
    @Test
    public void execute_invalidInputs_throwsException() {
        // Null argStr
        MissingArgException nullException = assertThrows(MissingArgException.class, () ->
                doneCmd.execute("done", null));
        assertTrue(nullException.getMessage().contains("Which one"));

        // Non-integer input
        CmdFormatException formatException = assertThrows(CmdFormatException.class, () ->
                doneCmd.execute("done", "abc"));
        assertTrue(formatException.getMessage().contains("index"));

        // Out-of-bounds index
        InvalidArgException oobException = assertThrows(InvalidArgException.class, () ->
                doneCmd.execute("done", "99"));
        assertTrue(oobException.getMessage().contains("don't have task"));

        // Negative index
        InvalidArgException negException = assertThrows(InvalidArgException.class, () ->
                doneCmd.execute("done", "-1"));
        assertTrue(negException.getMessage().contains("don't have task"));
    }

    /**
     * Tests that execute marks task as complete.
     */
    @Test
    public void execute_incompleteTask_marksAsComplete()
            throws CmdFormatException, MissingArgException, InvalidArgException {
        assertFalse(taskTracker.getTask(0).isCompleted());

        CmdOutput output = doneCmd.execute("done", "1");

        assertFalse(output.isError());
        assertTrue(taskTracker.getTask(0).isCompleted());
        assertTrue(output.getReply().get(0).contains("[X]"));

        taskTracker.getTask(0).complete();
        assertTrue(taskTracker.getTask(0).isCompleted());

        CmdOutput output2 = doneCmd.execute("done", "1");

        assertFalse(output2.isError());
        assertTrue(taskTracker.getTask(0).isCompleted());
        assertTrue(output2.getReply().get(0).contains("[X]"));
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsDone() {
        assertEquals("done", doneCmd.getCmdName());
    }

}
