package notjippity.commands;

import notjippity.tasks.TaskTracker;
import notjippity.io.Ui;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Task;

/**
 * Handles "undo" command logic and behaviour
 */
public class UndoCmd extends Command {

    private final String FORMAT_CMD = "Format: undo <Task Id>";
    private Ui ui;
    private TaskTracker taskTracker;

    /**
     * Returns a new instance of UndoCmd
     */
    public UndoCmd(Ui ui, TaskTracker taskTracker) {
        super("undo");
        this.ui = ui;
        this.taskTracker = taskTracker;
    }

    /**
     * Sets a task's completion status to incomplete with the task index
     * provided in argStr (follows the index numbering of list command)
     * @param cmdStr The command string
     * @param argStr The string of arguments
     * @throws CmdFormatException If the command is of an invalid format
     * @throws MissingArgException If there are missing arguments
     * @throws InvalidArgException If there are invalid arguments
     */
    @Override
    public void execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException, InvalidArgException {
        if (argStr == null) {
            throw new MissingArgException("Which one? (" + FORMAT_CMD + ")");
        }

        Task task;

        try {
            int index = Integer.parseInt(argStr);
            task = taskTracker.getTask(index - 1);
        } catch (NumberFormatException exception) {
            throw new CmdFormatException("Idk waddat, enter the index of the task as seen in the \"list\" command instead");
        } catch (IndexOutOfBoundsException exception) {
            throw new InvalidArgException("Uhhhh we don't have task #" + argStr + ", maybe check with \"list\" again?");
        }

        task.undo();
        ui.send(task.toString());
    }

}
