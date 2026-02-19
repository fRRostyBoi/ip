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
 * Contains JUnit tests for the DeleteCmd class.
 */
public class DeleteCmdTest {

    private TaskTracker taskTracker;
    private DeleteCmd deleteCmd;

    /**
     * Sets up a TaskTracker with test tasks before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
        taskTracker.addTask(new ToDo("Task 1"));
        taskTracker.addTask(new ToDo("Task 2"));
        taskTracker.addTask(new ToDo("Task 3"));
        deleteCmd = new DeleteCmd(taskTracker);
    }

    /**
     * Tests that execute throws appropriate exceptions for various invalid inputs.
     */
    @Test
    public void execute_invalidInputs_throwsException() {
        // Null argStr
        MissingArgException nullException = assertThrows(MissingArgException.class, () ->
                deleteCmd.execute("delete", null));
        assertTrue(nullException.getMessage().contains("Which one"));

        // Non-integer input
        CmdFormatException formatException = assertThrows(CmdFormatException.class, () ->
                deleteCmd.execute("delete", "abc"));
        assertTrue(formatException.getMessage().contains("index"));

        // Out-of-bounds index
        InvalidArgException oobException = assertThrows(InvalidArgException.class, () ->
                deleteCmd.execute("delete", "99"));
        assertTrue(oobException.getMessage().contains("don't have task"));

        // Negative index
        InvalidArgException negException = assertThrows(InvalidArgException.class, () ->
                deleteCmd.execute("delete", "-1"));
        assertTrue(negException.getMessage().contains("don't have task"));
    }

    /**
     * Tests that execute successfully deletes task with valid index.
     */
    @Test
    public void execute_validIndex_deletesTask() throws CmdFormatException, MissingArgException, InvalidArgException {
        assertEquals(3, taskTracker.getSize());

        CmdOutput output = deleteCmd.execute("delete", "2");

        assertFalse(output.isError());
        assertEquals(2, taskTracker.getSize());
        assertTrue(output.getReply().get(0).contains("Task 2"));
    }

    /**
     * Tests that execute deletes the correct task.
     */
    @Test
    public void execute_deleteFirstTask_correctTaskRemoved()
            throws CmdFormatException, MissingArgException, InvalidArgException {
        deleteCmd.execute("delete", "1");

        assertEquals(2, taskTracker.getSize());
        assertTrue(taskTracker.getTask(0).toString().contains("Task 2"));
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsDelete() {
        assertEquals("delete", deleteCmd.getCmdName());
    }

}
