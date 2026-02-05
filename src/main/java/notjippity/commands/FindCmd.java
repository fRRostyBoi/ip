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
        if (argStr == null) {
            throw new MissingArgException("Np, just tell me what to look for (" + FORMAT_CMD + ")");
        }

        // Filter out the tasks which are relevant to the given date, along with the actual list indices
        HashMap<Integer, Task> tasks = new HashMap<>();
        int listIndex = 1;
        int lastAddedIndex = 0;
        for (Task task : taskTracker.getTasks()) {
            if (task.matchesKeyword(argStr)) {
                tasks.put(listIndex, task);
                lastAddedIndex = listIndex;
            }

            listIndex++;
        }

        if (tasks.isEmpty()) {
            return List.of("Didn't find anything matching \"" + argStr + "\", sry man");
        }

        List<String> messages = new ArrayList<>();
        messages.add("Here's what I found matching \"" + argStr + "\":");

        // Print the list of tasks. Append spaces after
        // tasks indices with lesser digits so the
        // line formatting is preserved
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
