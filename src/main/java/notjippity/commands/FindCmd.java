package notjippity.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

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
    public List<String> execute(String cmdStr, String argStr) throws MissingArgException {
        handleMissingInput(argStr);

        HashMap<Integer, Task> tasks = getRelevantTasks(argStr);
        if (tasks.isEmpty()) {
            return List.of("Didn't find anything matching \"" + argStr + "\", sry man");
        }

        return convertToFoundList(tasks, argStr);
    }

    /**
     * Handles missing search input in argStr.
     *
     * @param argStr The argument string.
     * @throws MissingArgException If the arg string is null.
     */
    private void handleMissingInput(String argStr) throws MissingArgException {
        if (argStr == null) {
            throw new MissingArgException("Np, just tell me what to look for (" + FORMAT_CMD + ")");
        }
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

    /**
     * Returns the largest index amount the map of indices.
     *
     * @param tasks The map of indices to tasks.
     * @return The largest index.
     */
    private int getLargestIndex(HashMap<Integer, Task> tasks) {
        int largest = -1;

        for (int index : tasks.keySet()) {
            if (largest < index) {
                largest = index;
            }
        }

        return largest;
    }

    /**
     * Converts the list of tasks into a list of response messages.
     *
     * @param tasks The list of tasks.
     * @return A list of strings representing the tasks.
     */
    private List<String> convertToFoundList(HashMap<Integer, Task> tasks, String argStr) {
        List<String> messages = new ArrayList<>();
        messages.add("Here's what I found matching \"" + argStr + "\":");

        // For indices with leser digits, add buffer spaces to match highest number of digits.
        // Ensures all strings that come after the indices are flush.
        int lastAddedIndex = getLargestIndex(tasks);
        int maxDigits = 1 + (int) Math.floor(Math.log10(lastAddedIndex));
        for (int index : tasks.keySet()) {
            Task task = tasks.get(index);
            int curDigits = 1 + (int) Math.floor(Math.log10(index));

            StringBuilder indexStr = new StringBuilder(index + ". ");
            for (int i = 0; i < maxDigits - curDigits; i++) {
                indexStr.append(" ");
            }

            messages.add(indexStr.toString() + task);
        }

        return messages;
    }

}
