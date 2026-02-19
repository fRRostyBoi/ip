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
 * Contains JUnit tests for the EventCmd class.
 */
public class EventCmdTest {

    private TaskTracker taskTracker;
    private EventCmd eventCmd;

    /**
     * Sets up a fresh TaskTracker and EventCmd before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
        eventCmd = new EventCmd(taskTracker);
    }

    /**
     * Tests that execute throws MissingArgException for various missing argument scenarios.
     */
    @Test
    public void execute_missingArguments_throwsMissingArgException() {
        // Null argStr
        MissingArgException nullException = assertThrows(MissingArgException.class, () ->
                eventCmd.execute("event", null));
        assertTrue(nullException.getMessage().contains("what's this task called"));

        // Missing --from flag
        MissingArgException missingFromException = assertThrows(MissingArgException.class, () ->
                eventCmd.execute("event", "Team meeting --to 25/12/2026 1700"));
        assertTrue(missingFromException.getMessage().contains("--from")
                || missingFromException.getMessage().contains("--to"));

        // Missing --to flag
        MissingArgException missingToException = assertThrows(MissingArgException.class, () ->
                eventCmd.execute("event", "Team meeting --from 25/12/2026 0900"));
        assertTrue(missingToException.getMessage().contains("--from")
                || missingToException.getMessage().contains("--to"));

        // Starts with --from flag (missing task name)
        MissingArgException startsWithException = assertThrows(MissingArgException.class, () ->
                eventCmd.execute("event", "--from 25/12/2026 0900 --to 25/12/2026 1700"));
        assertTrue(startsWithException.getMessage().contains("what's this task called"));

        // Empty --from value
        MissingArgException emptyValueException = assertThrows(MissingArgException.class, () ->
                eventCmd.execute("event", "Meeting --from --to 25/12/2026 1700"));
        assertTrue(emptyValueException.getMessage().contains("--from")
                || emptyValueException.getMessage().contains("--to"));
    }

    /**
     * Tests that execute throws CmdFormatException for various format errors.
     */
    @Test
    public void execute_invalidFormat_throwsCmdFormatException() {
        // Flags in wrong order
        CmdFormatException wrongOrderException = assertThrows(CmdFormatException.class, () ->
                eventCmd.execute("event", "Meeting --to 25/12/2026 1700 --from 25/12/2026 0900"));
        assertTrue(wrongOrderException.getMessage().contains("--from before --to"));

        // Invalid date format
        CmdFormatException invalidDateException = assertThrows(CmdFormatException.class, () ->
                eventCmd.execute("event", "Meeting --from 25/12/2026 1700 --to "));
        assertTrue(invalidDateException.getMessage().contains("date format"));

        CmdFormatException invalidDateException2 = assertThrows(CmdFormatException.class, () ->
                eventCmd.execute("event", "Meeting --from 02371092 --to w/d/aw 1297"));
        assertTrue(invalidDateException2.getMessage().contains("date format"));
    }

    /**
     * Tests that execute successfully creates an event with valid input.
     */
    @Test
    public void execute_validInput_addsEventToTracker() throws CmdFormatException, MissingArgException {
        CmdOutput output = eventCmd.execute("event", "Team meeting --from 25/12/2026 0900 --to 25/12/2026 1700");

        assertFalse(output.isError());
        assertEquals(1, taskTracker.getSize());
        assertTrue(output.getReply().get(0).contains("Team meeting"));
        assertTrue(output.getReply().get(0).contains("25/12/2026 0900"));
        assertTrue(output.getReply().get(0).contains("25/12/2026 1700"));
    }

    /**
     * Tests that execute validation is case-insensitive for flags (validation only).
     * Note: The actual split requires lowercase flags, but validation accepts uppercase.
     */
    @Test
    public void execute_diffCaseFlags_addsEvent() throws CmdFormatException, MissingArgException {
        CmdOutput output = eventCmd.execute("event", "Meeting --fRoM 25/12/2026 0900 --to 25/12/2026 1700");
        CmdOutput output2 = eventCmd.execute("event", "Meeting --from 25/12/2026 0900 --TO 25/12/2026 1700");
        CmdOutput output3 = eventCmd.execute("event", "Meeting --FROM 25/12/2026 0900 --to 25/12/2026 1700");
        CmdOutput output4 = eventCmd.execute("event", "Meeting --from 25/12/2026 0900 --To 25/12/2026 1700");
        CmdOutput output5 = eventCmd.execute("event", "Meeting --FrOm 25/12/2026 0900 --tO 25/12/2026 1700");

        assertEquals(5, taskTracker.getSize());
        assertFalse(output.isError());
        assertFalse(output2.isError());
        assertFalse(output3.isError());
        assertFalse(output4.isError());
        assertFalse(output5.isError());
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsEvent() {
        assertEquals("event", eventCmd.getCmdName());
    }

}
