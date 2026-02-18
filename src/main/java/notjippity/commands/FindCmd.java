package notjippity.commands;

import java.util.HashMap;
import java.util.List;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;
import notjippity.utils.CmdValidator;
import notjippity.utils.ListFormatter;

/**
 * Handles "Find" command logic and behaviour.
 */
public class FindCmd extends Command {

    private static final String FORMAT_CMD = "Format: find <keyword>";

    private TaskTracker taskTracker;

    /**
     * Returns a new FindCmd instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public FindCmd(TaskTracker taskTracker) {
        super("find");
        this.taskTracker = taskTracker;
        assert this.taskTracker != null;
    }

    /**
     * Prints all tasks matching the keyword given in argStr.
     *
     * @param cmdStr The command string.
     * @param argStr The string of arguments.
     * @return The bot's response.
     * @throws MissingArgException If argStr is null.
     */
    @Override
    public CmdOutput execute(String cmdStr, String argStr) throws MissingArgException {
        CmdValidator.validateNotNull(argStr, "Np, just tell me what to look for (" + FORMAT_CMD + ")");

        HashMap<Integer, Task> tasks = getRelevantTasks(argStr);
        if (tasks.isEmpty()) {
            return new CmdOutput(false, List.of("Didn't find anything matching \"" + argStr + "\", sry man"));
        }

        return new CmdOutput(false,
                ListFormatter.formatTaskMap(tasks, "Here's what I found matching \"" + argStr + "\":"));
    }

    /**
     * Returns a map of tasks which match the keyword.
     *
     * @param argStr The argument string.
     * @return A map of Task Indices to Tasks which match the keyword.
     */
    private HashMap<Integer, Task> getRelevantTasks(String argStr) {
        HashMap<Integer, Task> tasks = new HashMap<>();

        int listIndex = 1;
        for (Task task : taskTracker.getTasks()) {
            if (task.matchesKeyword(argStr)) {
                tasks.put(listIndex, task);
            }
            listIndex++;
        }

        return tasks;
    }

}
