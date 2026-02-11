package notjippity.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Deadline;
import notjippity.tasks.Event;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

/**
 * Handles "list" command logic and behaviour.
 */
public class ListCmd extends Command {

    private static final String FORMAT_DATE = "dd/MM/yyyy";
    private static final String FORMAT_CMD = "Format: list [--date <" + FORMAT_DATE + ">]";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

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
    public List<String> execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
        if (argStr == null) {
            return executeNormal();
        } else if (argStr.toLowerCase().startsWith("--date")) {
            return executeWithDate(argStr);
        } else {
            throw new CmdFormatException("Uhhh idk waddat (" + FORMAT_CMD + ")");
        }
    }

    /**
     * Executes the normal behaviour.
     *
     * @return The bot's response.
     */
    private List<String> executeNormal() {
        if (taskTracker.getSize() == 0) {
            return List.of("Nothing here yet man, wanna add some stuff? (todo, deadline, event)");
        }

        HashMap<Integer, Task> tasks = new HashMap<>();

        int i = 1;
        for (Task task : taskTracker.getTasks()) {
            tasks.put(i, task);
            i++;
        }

        return convertToFoundList(tasks, "Here's what we have so far:");
    }

    /**
     * Executes the "--date" flag behaviour.
     *
     * @param argStr The string of arguments.
     * @return The bot's response.
     * @throws CmdFormatException  If the user input has an invalid format.
     * @throws MissingArgException If the user input has (a) missing argument(s).
     */
    private List<String> executeWithDate(String argStr) throws CmdFormatException, MissingArgException {
        LocalDate date = parseDate(argStr);

        // Filter out the tasks which are relevant to the given date, along with the actual list indices
        HashMap<Integer, Task> tasks = getRelevantTasks(date);
        String formattedInput = date.format(DATE_FORMATTER);
        if (tasks.isEmpty()) {
            return List.of("Didn't find anything on " + formattedInput
                    + " yet, wanna add some stuff? (deadline, event)");
        }

        return convertToFoundList(tasks, "Here's what we have on " + formattedInput + ":");
    }

    /**
     * Parses the date argument into a LocalDate object.
     *
     * @param argStr The argument string.
     * @return The LocalDate object parsed from the provided argument.
     * @throws MissingArgException If the arg string is null.
     */
    private LocalDate parseDate(String argStr) throws CmdFormatException, MissingArgException {
        if (argStr == null) {
            throw new MissingArgException("On which date? (" + FORMAT_CMD + ")");
        }

        String dateStr = argStr.replaceFirst("--date", "").trim();
        if (dateStr.isEmpty()) {
            throw new MissingArgException("On which date? (" + FORMAT_CMD + ")");
        }

        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new CmdFormatException("Sry bro can't understand that date format (" + FORMAT_DATE + ")");
        }
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
        int lastAddedIndex = 0;
        for (Task task : taskTracker.getTasks()) {
            if (task instanceof Deadline deadline) {
                if (deadline.hasDate(date)) {
                    tasks.put(listIndex, deadline);
                    lastAddedIndex = listIndex;
                }
            } else if (task instanceof Event event) {
                if (event.hasDate(date)) {
                    tasks.put(listIndex, event);
                    lastAddedIndex = listIndex;
                }
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
    private List<String> convertToFoundList(HashMap<Integer, Task> tasks, String headerMsg) {
        List<String> messages = new ArrayList<>();
        messages.add(headerMsg);

        // For indices with lesser digits, add buffer spaces to match highest number of digits.
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
