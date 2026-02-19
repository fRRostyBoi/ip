package notjippity.commands;

import java.time.LocalDateTime;
import java.util.List;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Deadline;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;
import notjippity.utils.CmdValidator;
import notjippity.utils.DateTimeUtils;

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
    public CmdOutput execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
        validateArguments(argStr);

        String[] argSets = argStr.trim().split("--(?i)by");
        CmdValidator.validateNotEmpty(argSets.length > 1 ? argSets[1].trim() : null,
                "Didja forget to put something at the back of --by? (" + FORMAT_CMD + ")");

        String taskName = argSets[0].trim();
        LocalDateTime byDate = DateTimeUtils.parseDateTime(argSets[1].trim(),
                Deadline.DATETIME_FORMATTER, FORMAT_CMD);

        Task task = new Deadline(taskName, byDate);
        taskTracker.addTask(task);

        return new CmdOutput(false, List.of("++ " + task + " (" + taskTracker.getSize() + " total)"));
    }

    /**
     * Validates that task name and --by flag are provided.
     *
     * @param argStr The argument string.
     * @throws MissingArgException If the arg string is null or missing flags.
     */
    private void validateArguments(String argStr) throws MissingArgException {
        if (argStr == null) {
            throw new MissingArgException("Sooo... what's this task called? (" + FORMAT_CMD + ")");
        }

        String argStringLow = argStr.toLowerCase();
        CmdValidator.validateNotStartsWith(argStringLow, "--by",
                "First things first, what's this task called? (" + FORMAT_CMD + ")");
        CmdValidator.validateContains(argStringLow, "--by",
                "Np but tell me when it's to be done by (" + FORMAT_CMD + ")");
    }
}
