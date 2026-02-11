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

    private static final String FORMAT_CMD = "Format: deadline <Name> --by <" + Deadline.FORMAT_DATE + ">";

    private TaskTracker taskTracker;

    /**
     * Returns a new DeadlineCmd instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public DeadlineCmd(TaskTracker taskTracker) {
        super("deadline");
        this.taskTracker = taskTracker;
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
        handleMissingName(argStr);
        handleMissingByFlag(argStr);

        String[] argSets = argStr.trim().split("--by");
        handleMissingByDate(argSets);

        String taskName = argSets[0].trim();
        LocalDateTime byDate = parseByDate(argSets[1].trim());
        Task task = new Deadline(taskName, byDate);
        taskTracker.addTask(task);

        return List.of("++ " + task + " (" + taskTracker.getSize() + " total)");
    }

    /**
     * Handles missing task name input in argStr.
     *
     * @param argStr The argument string.
     * @throws MissingArgException If the arg string is null or bank.
     */
    private void handleMissingName(String argStr) throws MissingArgException {
        // If the user input something like "deadline" or "deadline --by [...]"
        if (argStr == null) {
            throw new MissingArgException("Sooo... what's this task called? (" + FORMAT_CMD + ")");
        }
    }

    /**
     * Handles missing "--by" flag in argStr.
     *
     * @param argStr The argument string.
     * @throws MissingArgException If --by is missing in the argStr.
     */
    private void handleMissingByFlag(String argStr) throws MissingArgException {
        String argStringLow = argStr.toLowerCase();

        if (argStringLow.startsWith("--by")) {
            throw new MissingArgException("First things first, what's this task called? (" + FORMAT_CMD + ")");
        }

        if (!argStringLow.contains("--by")) {
            throw new MissingArgException("Np but tell me when it's to be done by (" + FORMAT_CMD + ")");
        }
    }

    /**
     * Handles missing date argument after the "--by" flag in argStr.
     *
     * @param argSets The argument string array, split using "--by".
     * @throws MissingArgException If the date is missing.
     */
    private void handleMissingByDate(String[] argSets) throws MissingArgException {
        if (argSets.length == 1) {
            throw new MissingArgException("Didja forget to put something at the back of --by? (" + FORMAT_CMD + ")");
        }
    }

    /**
     * Converts the date argument into a LocalDateTime object.
     *
     * @param byDateStr The date argument.
     * @return The LocalDateTime object.
     * @throws CmdFormatException If the date argument is not in the correct format.
     */
    private LocalDateTime parseByDate(String byDateStr) throws CmdFormatException {
        try {
            return LocalDateTime.parse(byDateStr, Deadline.DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new CmdFormatException("Follow the date format pls (" + FORMAT_CMD + ")");
        }
    }

}
