package notjippity.commands;

import notjippity.exceptions.MissingArgException;
import notjippity.io.Ui;
import notjippity.tasks.TaskTracker;
import org.junit.jupiter.api.Test;

import static notjippity.commands.ToDoCmd.FORMAT_CMD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ToDoCmdTest {

    @Test
    public void testExecute() {
        Ui ui = new Ui();
        TaskTracker taskTracker = new TaskTracker();

        // Test for null argStr
        try {
            new ToDoCmd(ui, taskTracker).execute("todo", null);
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }
        // Test for empty argStr
        try {
            new ToDoCmd(ui, taskTracker).execute("todo", "");
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }
        // Test for non-empty, but completely whitespace argStr
        try {
            new ToDoCmd(ui, taskTracker).execute("todo", "             ");
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }
        // Test for non-empty breakline argStr
        try {
            new ToDoCmd(ui, taskTracker).execute("todo", "\n");
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }
        // Test for non-empty whitespace-into-breakline argStr
        try {
            new ToDoCmd(ui, taskTracker).execute("todo", "     \n");
            fail();
        } catch (MissingArgException exception) {
            assertEquals("Sooo... what's this task called? (" + FORMAT_CMD + ")", exception.getMessage());
        }
        // Normal argStr case
        try {
            new ToDoCmd(ui, taskTracker).execute("todo", "test");
        } catch (MissingArgException exception) {
            fail();
        }
        // Normal argStr case 2
        try {
            new ToDoCmd(ui, taskTracker).execute("todo", "test one two three");
        } catch (MissingArgException exception) {
            fail();
        }
    }

}
