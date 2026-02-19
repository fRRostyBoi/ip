package notjippity.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Deadline;
import notjippity.tasks.Event;
import notjippity.tasks.TaskTracker;
import notjippity.tasks.ToDo;

/**
 * Contains JUnit tests for the ListCmd class.
 */
public class ListCmdTest {

    private TaskTracker taskTracker;
    private ListCmd listCmd;

    /**
     * Sets up a fresh TaskTracker and ListCmd before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
        listCmd = new ListCmd(taskTracker);
    }

    /**
     * Tests that execute returns empty message for empty task list.
     */
    @Test
    public void execute_emptyTaskList_returnsEmptyMessage() throws CmdFormatException, MissingArgException {
        CmdOutput output = listCmd.execute("list", null);

        assertFalse(output.isError());
        assertTrue(output.getReply().get(0).contains("Nothing here yet"));
    }

    /**
     * Tests that execute returns all tasks when no arguments provided.
     */
    @Test
    public void execute_withTasks_returnsAllTasks() throws CmdFormatException, MissingArgException {
        taskTracker.addTask(new ToDo("Task 1"));
        taskTracker.addTask(new ToDo("Task 2"));

        CmdOutput output = listCmd.execute("list", null);

        assertFalse(output.isError());
        assertTrue(output.getReply().size() > 1); // Header + tasks
        assertTrue(output.getReply().get(0).contains("Here's what we have"));
    }

    /**
     * Tests that execute with --date flag filters by date correctly, even if none are found.
     */
    @Test
    public void execute_withDateFlag_filtersTasksByDate() throws CmdFormatException, MissingArgException {
        LocalDateTime targetDate = LocalDateTime.of(2026, 3, 15, 14, 30);
        taskTracker.addTask(new Deadline("Deadline on target date", targetDate));
        taskTracker.addTask(new ToDo("Regular todo"));

        CmdOutput haveOutput = listCmd.execute("list", "--date 15/03/2026");

        assertFalse(haveOutput.isError());
        assertTrue(haveOutput.getReply().stream().anyMatch(s -> s.contains("Deadline on target date")));

        CmdOutput noneOutput = listCmd.execute("list", "--date 15/03/2027");

        assertFalse(noneOutput.isError());
        assertTrue(noneOutput.getReply().get(0).contains("Didn't find anything"));

    }

    /**
     * Tests that execute with case-insensitive --date flag filters by date correctly, even if none are found.
     */
    @Test
    public void execute_diffCaseFlag_filtersTasksByDate() throws CmdFormatException, MissingArgException {
        LocalDateTime targetDate = LocalDateTime.of(2026, 3, 15, 14, 30);
        taskTracker.addTask(new Deadline("Deadline on target date", targetDate));
        taskTracker.addTask(new ToDo("Regular todo"));

        CmdOutput haveOutput = listCmd.execute("list", "--DATE 15/03/2026");

        assertFalse(haveOutput.isError());
        assertTrue(haveOutput.getReply().stream().anyMatch(s -> s.contains("Deadline on target date")));

        CmdOutput noneOutput = listCmd.execute("list", "--DaTe 15/03/2027");

        assertFalse(noneOutput.isError());
        assertTrue(noneOutput.getReply().get(0).contains("Didn't find anything"));
    }

    /**
     * Tests that execute throws appropriate exceptions for invalid inputs.
     */
    @Test
    public void execute_invalidInputs_throwsException() {
        // Invalid date format
        CmdFormatException formatException = assertThrows(CmdFormatException.class, () ->
                listCmd.execute("list", "--date 128/128/128 128128:128"));
        assertTrue(formatException.getMessage().contains("date format"));

        // --date flag with no value
        MissingArgException missingException = assertThrows(MissingArgException.class, () ->
                listCmd.execute("list", "--date"));
        assertTrue(missingException.getMessage().contains("which date")
                || missingException.getMessage().contains("On which date"));

        // Unknown argument
        CmdFormatException unknownException = assertThrows(CmdFormatException.class, () ->
                listCmd.execute("list", "--unknown"));
        assertTrue(unknownException.getMessage().contains("idk"));
    }

    /**
     * Tests that execute filters events within date range.
     */
    @Test
    public void execute_dateWithinEventRange_findsEvent() throws CmdFormatException, MissingArgException {
        LocalDateTime from = LocalDateTime.of(2026, 3, 10, 9, 0);
        LocalDateTime to = LocalDateTime.of(2026, 3, 20, 17, 0);
        taskTracker.addTask(new Event("Multi-day event", from, to));

        // Search for a date in the middle of the event range
        CmdOutput output = listCmd.execute("list", "--date 15/03/2026");

        assertFalse(output.isError());
        assertTrue(output.getReply().stream().anyMatch(s -> s.contains("Multi-day event")));
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsList() {
        assertEquals("list", listCmd.getCmdName());
    }

}
