package notjippity.commands;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.io.Ui;
import notjippity.tasks.Deadline;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Handles "Deadline" command logic and behaviour
 */
public class DeadlineCmd extends Command {

    private static final String FORMAT_CMD = "Format: deadline <Name> --by <" + Deadline.DATE_FORMAT + ">";

    private Ui ui;
    private TaskTracker taskTracker;

    /**
     * Returns a new instance of DeadlineCmd
     */
    public DeadlineCmd(Ui ui, TaskTracker taskTracker) {
        super("deadline");
        this.ui = ui;
        this.taskTracker = taskTracker;
    }

    /**
     * Adds a Deadline task into the tasklist and executes feedback
     *
     * @param argStr User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     */
    @Override
    public void execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
        // If the user input something like "deadline" or "deadline --by [...]"
        if (argStr == null) {
            throw new MissingArgException("First things first, what's this task called? (" + FORMAT_CMD + ")");
        }
        String argStringLow = argStr.toLowerCase();
        if (argStringLow.startsWith("--by")) {
            throw new MissingArgException("First things first, what's this task called? (" + FORMAT_CMD + ")");
        }

        // If the user input doesn't contain "--by"
        if (!argStringLow.contains("--by")) {
            throw new MissingArgException("Np but tell me when it's to be done by (" + FORMAT_CMD + ")");
        }

        String[] argSets = argStr.trim().split("--by");
        // If the user input doesn't specify the date after "--by"
        if (argSets.length == 1) {
            throw new MissingArgException("Didja forget to put something at the back of --by? (" + FORMAT_CMD + ")");
        }

        String taskName = argSets[0].trim(),
                byDateStr = argSets[1].trim();
        LocalDateTime byDate;

        try {
            byDate = LocalDateTime.parse(byDateStr, Deadline.DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new CmdFormatException("Follow the date format pls (" + FORMAT_CMD + ")");
        }

        Task task = new Deadline(taskName, byDate);
        taskTracker.addTask(task);
        ui.send("++ " + task + " (" + taskTracker.getSize() + " total)");
    }

}
