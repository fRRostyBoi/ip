package notjippity.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.io.Ui;
import notjippity.tasks.Event;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

/**
 * Handles "Event" command logic and behaviour
 */
public class EventCmd extends Command {

    private static final String FORMAT_CMD = "Format: event <Name> --from <" + Event.FORMAT_DATE
            + "> --to <" + Event.FORMAT_DATE + ">";

    private Ui ui;
    private TaskTracker taskTracker;

    /**
     * Returns a new EventCommand instance
     *
     * @param ui The bot's UI
     * @param taskTracker The bot's task tracker
     */
    public EventCmd(Ui ui, TaskTracker taskTracker) {
        super("event");
        this.ui = ui;
        this.taskTracker = taskTracker;
    }

    /**
     * Adds an Event task into the tasklist and executes feedback
     *
     * @param argStr User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     * @throws CmdFormatException If flags are in the wrong order
     */
    @Override
    public void execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
        // If the user input something like "event", "event --from [...]" or "event --to [...]"
        if (argStr == null) {
            throw new MissingArgException("First things first, what's this task called? (" + FORMAT_CMD + ")");
        }
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

        String[] exclNameArgs = argStr.trim().split("--from");
        String taskName = exclNameArgs[0].trim(), exclFromArgs = exclNameArgs[1].trim();
        String[] exclToArgs = exclFromArgs.split("--to");
        String fromStr = exclToArgs[0].trim(), toStr = exclToArgs[1].trim();
        LocalDateTime fromDate, toDate;

        // If the arguments following --from or --to is empty
        if (fromStr.isEmpty() || toStr.isEmpty()) {
            throw new MissingArgException("Didja forget to put something at the back of --from or --to? (" + FORMAT_CMD + ")");
        }

        try {
            fromDate = LocalDateTime.parse(fromStr, Event.DATETIME_FORMATTER);
            toDate = LocalDateTime.parse(toStr, Event.DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new CmdFormatException("Follow the date format pls (" + FORMAT_CMD + ")");
        }

        Task task = new Event(taskName, fromDate, toDate);
        taskTracker.addTask(task);
        ui.send("++ " + task + " (" + taskTracker.getSize() + " tasks)");
    }

}
