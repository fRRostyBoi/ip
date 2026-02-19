package notjippity.commands;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Deadline;
import notjippity.tasks.Event;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;
import notjippity.utils.DateTimeUtils;
import notjippity.utils.ListFormatter;

/**
 * Handles "list" command logic and behaviour.
 */
public class ListCmd extends Command {

    private static final String FORMAT_DATE = "dd/MM/yyyy";
    private static final String FORMAT_CMD = "Format: list [--date <" + FORMAT_DATE + ">]";

    private TaskTracker taskTracker;

    /**
     * Returns a new ListCmd instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public ListCmd(TaskTracker taskTracker) {
        super("list");
        this.taskTracker = taskTracker;
        assert this.taskTracker != null;
    }

    /**
     * Prints the list of all tasks currently stored, or only those occurring
     * on a specific date if the --date flag is included.
     *
     * @param cmdStr The command string.
     * @param argStr The string of arguments.
     * @return The bot's response.
     * @throws CmdFormatException  If the user input has an invalid format.
     * @throws MissingArgException If the user input has (a) missing argument(s).
     */
    @Override
    public CmdOutput execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
        if (argStr == null) {
            return executeNormal();
        }

        if (argStr.toLowerCase().startsWith("--date")) {
            System.out.println("Yes");
            return executeWithDate(argStr);
        }

        throw new CmdFormatException("Uhhh idk waddat (" + FORMAT_CMD + ")");
    }

    /**
     * Executes the normal behaviour.
     *
     * @return The bot's response.
     */
    private CmdOutput executeNormal() {
        if (taskTracker.getSize() == 0) {
            return new CmdOutput(false, List.of("Nothing here yet man, wanna add some stuff? (todo, deadline, event)"));
        }

        HashMap<Integer, Task> tasks = buildTaskMap();
        return new CmdOutput(false, ListFormatter.formatTaskMap(tasks, "Here's what we have so far:"));
    }

    /**
     * Executes the "--date" flag behaviour.
     *
     * @param argStr The string of arguments.
     * @return The bot's response.
     * @throws CmdFormatException  If the user input has an invalid format.
     * @throws MissingArgException If the user input has (a) missing argument(s).
     */
    private CmdOutput executeWithDate(String argStr) throws CmdFormatException, MissingArgException {
        LocalDate date = DateTimeUtils.parseDate(argStr, "--date", FORMAT_DATE, FORMAT_CMD);

        HashMap<Integer, Task> tasks = getRelevantTasks(date);
        String formattedInput = DateTimeUtils.formatDate(date, FORMAT_DATE);

        if (tasks.isEmpty()) {
            return new CmdOutput(false, List.of("Didn't find anything on " + formattedInput
                    + " yet, wanna add some stuff? (deadline, event)"));
        }

        return new CmdOutput(false,
                ListFormatter.formatTaskMap(tasks, "Here's what we have on " + formattedInput + ":"));
    }

    /**
     * Builds a map of tasks with their corresponding list indices.
     *
     * @return A map of Task Indices to Tasks.
     */
    private HashMap<Integer, Task> buildTaskMap() {
        HashMap<Integer, Task> tasks = new HashMap<>();
        int index = 1;
        for (Task task : taskTracker.getTasks()) {
            tasks.put(index++, task);
        }
        return tasks;
    }

    /**
     * Returns a map of tasks which match or contains the given date.
     *
     * @param date The LocalDate to compare with.
     * @return A map of Task Indices to Tasks which match the keyword.
     */
    private HashMap<Integer, Task> getRelevantTasks(LocalDate date) {
        HashMap<Integer, Task> tasks = new HashMap<>();

        int listIndex = 1;
        for (Task task : taskTracker.getTasks()) {
            if (isTaskOnDate(task, date)) {
                tasks.put(listIndex, task);
            }
            listIndex++;
        }

        return tasks;
    }

    /**
     * Checks if a task occurs on the given date.
     *
     * @param task The task to check.
     * @param date The date to compare with.
     * @return True if the task occurs on the date, false otherwise.
     */
    private boolean isTaskOnDate(Task task, LocalDate date) {
        if (task instanceof Deadline deadline) {
            return deadline.hasDate(date);
        }

        if (task instanceof Event event) {
            return event.hasDate(date);
        }

        return false;
    }
}
