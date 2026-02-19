package notjippity.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.TaskTracker;
import notjippity.tasks.ToDo;

/**
 * Contains JUnit tests for the FindCmd class.
 */
public class FindCmdTest {

    private TaskTracker taskTracker;
    private FindCmd findCmd;

    /**
     * Sets up a TaskTracker with test tasks before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
        taskTracker.addTask(new ToDo("Buy groceries"));
        taskTracker.addTask(new ToDo("Read book"));
        taskTracker.addTask(new ToDo("Buy milk"));
        findCmd = new FindCmd(taskTracker);
    }

    /**
     * Tests that execute throws MissingArgException for null argStr.
     */
    @Test
    public void execute_nullArgStr_throwsMissingArgException() {
        MissingArgException exception = assertThrows(MissingArgException.class, () ->
                findCmd.execute("find", null));
        assertTrue(exception.getMessage().contains("tell me what to look for"));
    }

    /**
     * Tests that execute returns matching tasks for various search scenarios.
     */
    @Test
    public void execute_variousSearchKeywords_returnsCorrectResults() throws MissingArgException {
        // Matching keyword
        CmdOutput matchOutput = findCmd.execute("find", "Buy");
        assertFalse(matchOutput.isError());
        assertTrue(matchOutput.getReply().size() > 1);
        assertTrue(matchOutput.getReply().get(0).contains("found"));

        // Case-insensitive
        CmdOutput caseOutput = findCmd.execute("find", "buy");
        assertFalse(caseOutput.isError());
        assertTrue(caseOutput.getReply().size() > 1);

        // Partial match
        CmdOutput partialOutput = findCmd.execute("find", "groc");
        assertFalse(partialOutput.isError());
        assertTrue(partialOutput.getReply().size() > 1);

        // Partial match case-insensitive
        CmdOutput partialOutput2 = findCmd.execute("find", "UY grO");
        assertFalse(partialOutput2.isError());
        assertTrue(partialOutput2.getReply().size() > 1);

        // No matching keyword
        CmdOutput noMatchOutput = findCmd.execute("find", "xyz123");
        assertFalse(noMatchOutput.isError());
        assertTrue(noMatchOutput.getReply().get(0).contains("Didn't find anything"));
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsFind() {
        assertEquals("find", findCmd.getCmdName());
    }

}
