package notjippity.commands;

import java.util.List;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

/**
 * Handles "delete" command logic and behaviour.
 */
public class DeleteCmd extends Command {

    private static final String FORMAT_CMD = "Format: delete <Task Id>";

    private TaskTracker taskTracker;

    /**
     * Returns a new DeleteCmd instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public DeleteCmd(TaskTracker taskTracker) {
        super("delete");
        this.taskTracker = taskTracker;
    }

    /**
     * Deletes a task with the task index provided in argStr.
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
        handleMissingTask(argStr);

        Task task = checkAndReturnValidTask(argStr);
        taskTracker.removeTask(task);

        return List.of("-- #" + argStr + " " + task.toString());
    }

    /**
     * Handles missing task input in argStr.
     *
     * @param argStr The argument string.
     * @throws MissingArgException If the arg string is null.
     */
    private void handleMissingTask(String argStr) throws MissingArgException {
        if (argStr == null) {
            throw new MissingArgException("Which one? (" + FORMAT_CMD + ")");
        }
    }

    /**
     * Returns the valid task, handling the logic for error cases.
     *
     * @param argStr The argument string.
     * @return The Task object.
     * @throws CmdFormatException  If argStr does not contain a valid integer
     * @throws InvalidArgException If argStr does not contain a valid index
     */
    private Task checkAndReturnValidTask(String argStr) throws CmdFormatException, InvalidArgException {
        try {
            int index = Integer.parseInt(argStr);
            return taskTracker.getTask(index - 1);
        } catch (NumberFormatException exception) {
            throw new CmdFormatException("Idk waddat, enter the index of the task as seen "
                    + "in the \"list\" command instead");
        } catch (IndexOutOfBoundsException exception) {
            throw new InvalidArgException("Uhhhh we don't have task #" + argStr
                    + ", maybe check with \"list\" again?");
        }
    }

}
