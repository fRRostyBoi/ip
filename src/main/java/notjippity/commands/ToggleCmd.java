package notjippity.commands;

import java.util.List;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

/**
 * Handles "toggle" command logic and behaviour.
 */
public class ToggleCmd extends Command {

    private static final String FORMAT_CMD = "Format: toggle <Task Id>";

    private TaskTracker taskTracker;

    /**
     * Returns a new ToggleCmd instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public ToggleCmd(TaskTracker taskTracker) {
        super("toggle");
        this.taskTracker = taskTracker;
        assert this.taskTracker != null;
    }

    /**
     * Toggles a task's completion status with the task index provided in argStr.
     * (follows the index numbering of list command).
     *
     * @param cmdStr The command string.
     * @param argStr The string of arguments.
     * @return The bot's response.
     * @throws CmdFormatException  If the command is of an invalid format.
     * @throws MissingArgException If there are missing arguments.
     * @throws InvalidArgException If there are invalid arguments.
     */
    @Override
    public List<String> execute(String cmdStr, String argStr) throws
            CmdFormatException, MissingArgException, InvalidArgException {
        if (argStr == null) {
            throw new MissingArgException("Which one? (" + FORMAT_CMD + ")");
        }

        Task task;

        try {
            int index = Integer.parseInt(argStr);
            task = taskTracker.getTask(index - 1);
        } catch (NumberFormatException exception) {
            throw new CmdFormatException("Idk waddat, enter the index of the task as seen "
                    + "in the \"list\" command instead");
        } catch (IndexOutOfBoundsException exception) {
            throw new InvalidArgException("Uhhhh we don't have task #" + argStr
                    + ", maybe check with \"list\" again?");
        }

        task.toggleComplete();
        return List.of(task.toString());
    }

}
