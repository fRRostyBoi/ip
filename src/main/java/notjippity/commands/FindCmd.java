package notjippity.commands;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.io.Ui;
import notjippity.tasks.Deadline;
import notjippity.tasks.Event;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

/**
 * Handles "Find" command logic and behaviour
 */
public class FindCmd extends Command {

    private static final String FORMAT_CMD = "Format: find <keyword>";

    private Ui ui;
    private TaskTracker taskTracker;

    /**
     * Returns a new FindCmd instance
     */
    public FindCmd(Ui ui, TaskTracker taskTracker) {
        super("find");
        this.ui = ui;
        this.taskTracker = taskTracker;
    }

    /**
     * Prints all tasks matching the keyword given in argStr
     *
     * @param cmdStr The command string
     * @param argStr The string of arguments
     * @throws MissingArgException If argStr is null
     */
    @Override
    public void execute(String cmdStr, String argStr) throws MissingArgException {
        if (argStr == null) {
            throw new MissingArgException("Np, just tell me what to look for (" + FORMAT_CMD + ")");
        }

        // Filter out the tasks which are relevant to the given date, along with the actual list indices
        HashMap<Integer, Task> tasks = new HashMap<>();
        int listIndex = 1, lastAddedIndex = 0;
        for (Task task : taskTracker.getTasks()) {
            if (task.matchesKeyword(argStr)) {
                tasks.put(listIndex, task);
                lastAddedIndex = listIndex;
            }

            listIndex++;
        }

        if (tasks.isEmpty()) {
            ui.send("Didn't find anything matching \"" + argStr + "\", sry man");
            return;
        }

        ui.send("Here's what I found matching \"" + argStr + "\":");

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
            ui.sendWithSpacer(indexStr.toString() + task);
        }
    }


}
