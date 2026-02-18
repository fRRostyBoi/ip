package notjippity.commands;

import java.util.List;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;
import notjippity.utils.IndexValidator;

/**
 * Handles "undo" command logic and behaviour.
 */
public class UndoCmd extends Command {

    private TaskTracker taskTracker;

    /**
     * Returns a new UndoCmd instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public UndoCmd(TaskTracker taskTracker) {
        super("undo");
        this.taskTracker = taskTracker;
        assert this.taskTracker != null;
    }

    /**
     * Sets a task's completion status to incomplete with the task index
     * provided in argStr (follows the index numbering of list command).
     *
     * @param cmdStr The command string.
     * @param argStr The string of arguments.
     * @return The bot's response.
     * @throws CmdFormatException  If the command is of an invalid format.
     * @throws MissingArgException If there are missing arguments.
     * @throws InvalidArgException If there are invalid arguments.
     */
    @Override
    public CmdOutput execute(String cmdStr, String argStr) throws
            CmdFormatException, MissingArgException, InvalidArgException {
        IndexValidator.validateNotMissing(argStr, "undo");

        Task task = IndexValidator.getValidTask(argStr, taskTracker);
        task.undo();

        return new CmdOutput(false, List.of(task.toString()));
    }
}
