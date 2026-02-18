package notjippity.commands;

import java.time.LocalDateTime;
import java.util.List;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Deadline;
import notjippity.tasks.Event;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;
import notjippity.utils.CmdValidator;
import notjippity.utils.DateTimeUtils;

/**
 * Handles "Event" command logic and behaviour.
 */
public class EventCmd extends Command {

    private static final String FORMAT_CMD = "Format: event <Name> --from <" + Event.FORMAT_DATE
            + "> --to <" + Event.FORMAT_DATE + ">";

    private TaskTracker taskTracker;

    /**
     * Returns a new EventCommand instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public EventCmd(TaskTracker taskTracker) {
        super("event");
        this.taskTracker = taskTracker;
        assert this.taskTracker != null;
    }

    /**
     * Adds an Event task into the tasklist.
     *
     * @param argStr User's input command arguments.
     * @return The bot's response.
     * @throws MissingArgException If user input is missing any arguments.
     * @throws CmdFormatException  If flags are in the wrong order.
     */
    @Override
    public CmdOutput execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
        validateArguments(argStr);

        String[] values = extractValuesFromFlags(argStr);
        String taskName = values[0];
        String fromStr = values[1];
        String toStr = values[2];

        CmdValidator.validateNotEmpty(fromStr, "Didja forget to put something at the back of "
                + "--from or --to? (" + FORMAT_CMD + ")");
        CmdValidator.validateNotEmpty(toStr, "Didja forget to put something at the back of "
                + "--from or --to? (" + FORMAT_CMD + ")");

        LocalDateTime fromDate = DateTimeUtils.parseDateTime(fromStr, Deadline.DATETIME_FORMATTER, FORMAT_CMD);
        LocalDateTime toDate = DateTimeUtils.parseDateTime(toStr, Deadline.DATETIME_FORMATTER, FORMAT_CMD);

        Task task = new Event(taskName, fromDate, toDate);
        taskTracker.addTask(task);

        return new CmdOutput(false, List.of("++ " + task + " (" + taskTracker.getSize() + " tasks)"));
    }

    /**
     * Validates that task name and flags are provided correctly.
     *
     * @param argStr The argument string.
     * @throws MissingArgException If the arg string is null or missing flags.
     * @throws CmdFormatException  If flags are in the wrong order.
     */
    private void validateArguments(String argStr) throws CmdFormatException, MissingArgException {
        if (argStr == null) {
            throw new MissingArgException("First things first, what's this task called? (" + FORMAT_CMD + ")");
        }

        String argStringLow = argStr.trim().toLowerCase();

        CmdValidator.validateNotStartsWith(argStringLow, "--from",
                "First things first, what's this task called? (" + FORMAT_CMD + ")");
        CmdValidator.validateNotStartsWith(argStringLow, "--to",
                "First things first, what's this task called? (" + FORMAT_CMD + ")");

        CmdValidator.validateContains(argStringLow, "--from",
                "Np but tell me when it's to be done by (" + FORMAT_CMD + ")");
        CmdValidator.validateContains(argStringLow, "--to",
                "Np but tell me when it's to be done by (" + FORMAT_CMD + ")");

        CmdValidator.validateOrder(argStringLow, "--from", "--to",
                "Write --from before --to pls (" + FORMAT_CMD + ")");
    }

    /**
     * Extracts the values after each flag and returns it in order.
     *
     * @param argStr The argument string.
     * @return The task name, from flag value, to flag value in that order, as an array .
     */
    private String[] extractValuesFromFlags(String argStr) {
        // Attempts to extract task name, "from" and "to" value arguments in order
        String[] exclNameArgs = argStr.trim().split("--from");
        String taskName = exclNameArgs[0].trim();
        String exclFromArgs = exclNameArgs[1].trim();

        String[] exclToArgs = exclFromArgs.split("--to");
        String fromStr = exclToArgs[0].trim();
        String toStr = exclToArgs[1].trim();

        return new String[]{taskName, fromStr, toStr};
    }
}