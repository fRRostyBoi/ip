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
 * Contains JUnit tests for the UndoCmd class.
 */
public class UndoCmdTest {

    private TaskTracker taskTracker;
    private UndoCmd undoCmd;

    /**
     * Sets up a TaskTracker with test tasks before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
        ToDo task1 = new ToDo("Task 1");
        task1.complete(); // Pre-complete for testing
        taskTracker.addTask(task1);

        taskTracker.addTask(new ToDo("Task 2"));
        undoCmd = new UndoCmd(taskTracker);
    }

    /**
     * Tests that execute throws appropriate exceptions for various invalid inputs.
     */
    @Test
    public void execute_invalidInputs_throwsException() {
        // Null argStr
        MissingArgException nullException = assertThrows(MissingArgException.class, () ->
                undoCmd.execute("undo", null));
        assertTrue(nullException.getMessage().contains("Which one"));

        // Non-integer input
        CmdFormatException formatException = assertThrows(CmdFormatException.class, () ->
                undoCmd.execute("undo", "abc"));
        assertTrue(formatException.getMessage().contains("index"));

        // Out-of-bounds index
        InvalidArgException oobException = assertThrows(InvalidArgException.class, () ->
                undoCmd.execute("undo", "99"));
        assertTrue(oobException.getMessage().contains("don't have task"));

        // Negative index
        InvalidArgException negException = assertThrows(InvalidArgException.class, () ->
                undoCmd.execute("undo", "-1"));
        assertTrue(negException.getMessage().contains("don't have task"));
    }

    /**
     * Tests that execute marks task as incomplete.
     */
    @Test
    public void execute_completeTask_marksAsIncomplete()
            throws CmdFormatException, MissingArgException, InvalidArgException {
        assertTrue(taskTracker.getTask(0).isCompleted());

        CmdOutput output = undoCmd.execute("undo", "1");

        assertFalse(output.isError());
        assertFalse(taskTracker.getTask(0).isCompleted());
        assertTrue(output.getReply().get(0).contains("[  ]"));

        CmdOutput output2 = undoCmd.execute("undo", "2");

        assertFalse(output2.isError());
        assertFalse(taskTracker.getTask(0).isCompleted());
        assertTrue(output2.getReply().get(0).contains("[  ]"));
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsUndo() {
        assertEquals("undo", undoCmd.getCmdName());
    }

}
