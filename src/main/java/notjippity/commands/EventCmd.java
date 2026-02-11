package notjippity.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Deadline;
import notjippity.tasks.Event;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

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
    public List<String> execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
        handleMissingName(argStr);
        handleMissingFromToFlag(argStr);

        String[] values = extractValuesFromFlags(argStr);
        String taskName = values[0];
        String fromStr = values[1];
        String toStr = values[2];

        handleMissingFromToDate(fromStr, toStr);

        LocalDateTime fromDate = parseDate(fromStr);
        LocalDateTime toDate = parseDate(toStr);
        Task task = new Event(taskName, fromDate, toDate);
        taskTracker.addTask(task);

        return List.of("++ " + task + " (" + taskTracker.getSize() + " tasks)");
    }

    /**
     * Handles missing task name input in argStr.
     *
     * @param argStr The argument string.
     * @throws MissingArgException If the arg string is null or bank.
     */
    private void handleMissingName(String argStr) throws MissingArgException {
        // If the user input something like "event", "event --from [...]" or "event --to [...]"
        if (argStr == null) {
            throw new MissingArgException("First things first, what's this task called? (" + FORMAT_CMD + ")");
        }
    }

    /**
     * Handles missing or disorderly the "--from" or "--to" flag(s) in argStr.
     *
     * @param argStr The argument string.
     * @throws MissingArgException If --from or --to flag(s) is/are missing in the argStr, or in the incorrect order.
     */
    private void handleMissingFromToFlag(String argStr) throws CmdFormatException, MissingArgException {
        String argStringLow = argStr.trim().toLowerCase();
        if (argStringLow.startsWith("--from") || argStringLow.startsWith("--to")) {
            throw new MissingArgException("First things first, what's this task called? (" + FORMAT_CMD + ")");
        }

        // If the user input doesn't contain "--from" or "--to"
        if (!argStringLow.contains("--from") || !argStringLow.contains("--to")) {
            throw new MissingArgException("Np but tell me when it's to be done by (" + FORMAT_CMD + ")");
        }

        // If the user input has "--to" preceding "--from"
        if (argStringLow.indexOf("--from") > argStringLow.indexOf("--to")) {
            throw new CmdFormatException("Write --from before --to pls (" + FORMAT_CMD + ")");
        }
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

    /**
     * Handles missing date argument(s) after the "--from" or "--to" flags in argStr.
     *
     * @param fromStr The fromStr argument.
     * @param toStr   The toStr argument.
     * @throws MissingArgException If the date is missing.
     */
    private void handleMissingFromToDate(String fromStr, String toStr) throws MissingArgException {
        if (fromStr.isEmpty() || toStr.isEmpty()) {
            throw new MissingArgException("Didja forget to put something at the back of "
                    + "--from or --to? (" + FORMAT_CMD + ")");
        }
    }

    /**
     * Converts the date argument into a LocalDateTime object.
     *
     * @param byDateStr The date argument.
     * @return The LocalDateTime object.
     * @throws CmdFormatException If the date argument is not in the correct format.
     */
    private LocalDateTime parseDate(String byDateStr) throws CmdFormatException {
        try {
            return LocalDateTime.parse(byDateStr, Deadline.DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new CmdFormatException("Follow the date format pls (" + FORMAT_CMD + ")");
        }
    }

}
