package notjippity.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Deadline;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

/**
 * Handles "Deadline" command logic and behaviour.
 */
public class DeadlineCmd extends Command {

    private static final String FORMAT_CMD = "Format: deadline <Name> --by <" + Deadline.DATE_FORMAT + ">";

    private TaskTracker taskTracker;

    /**
     * Returns a new DeadlineCmd instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public DeadlineCmd(TaskTracker taskTracker) {
        super("deadline");
        this.taskTracker = taskTracker;

        assert this.taskTracker != null;
    }

    /**
     * Adds a Deadline task into the tasklist.
     *
     * @param cmdStr The command string.
     * @param argStr User's input command arguments.
     * @return The bot's response.
     * @throws CmdFormatException  If user input is of an invalid format.
     * @throws MissingArgException If user input is missing any arguments.
     */
    @Override
    public List<String> execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
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

        String taskName = argSets[0].trim();
        String byDateStr = argSets[1].trim();
        LocalDateTime byDate;

        try {
            byDate = LocalDateTime.parse(byDateStr, Deadline.DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new CmdFormatException("Follow the date format pls (" + FORMAT_CMD + ")");
        }

        Task task = new Deadline(taskName, byDate);
        taskTracker.addTask(task);
        return List.of("++ " + task + " (" + taskTracker.getSize() + " total)");
    }

}
