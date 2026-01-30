package notjippity.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * Handles "list" command logic and behaviour
 */
public class ListCmd extends Command {

    private static final String FORMAT_DATE = "dd/MM/yyyy";
    private static final String FORMAT_CMD = "Format: list [--date <" + FORMAT_DATE + ">]";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

    private Ui ui;
    private TaskTracker taskTracker;

    /**
     * Returns a new ListCmd instance
     *
     * @param ui The bot's UI
     * @param taskTracker The bot's task tracker
     */
    public ListCmd(Ui ui, TaskTracker taskTracker) {
        super("list");
        this.ui = ui;
        this.taskTracker = taskTracker;
    }

    /**
     * Prints the list of all tasks currently stored, or only those occurring
     * on a specific date if the --date flag is included
     *
     * @param cmdStr The command string
     * @param argStr The string of arguments
     * @throws CmdFormatException If the user input has an invalid format
     * @throws MissingArgException If the user input has (a) missing argument(s)
     */
    @Override
    public void execute(String cmdStr, String argStr) throws CmdFormatException, MissingArgException {
        if (argStr == null) {
            if (taskTracker.getSize() == 0) {
                ui.send("Nothing here yet man, wanna add some stuff? (todo, deadline, event)");
                return;
            }

            ui.send("Here's what we have so far:");

            int maxDigits = 1 + (int) Math.floor(Math.log10(taskTracker.getSize()));

            // Print the list of tasks. Append spaces after tasks indices with lesser digits so the
            // line formatting is preserved
            int index = 1;
            for (Task task : taskTracker.getTasks()) {
                int curDigits = 1 + (int) Math.floor(Math.log10(index));
                StringBuilder indexStr = new StringBuilder(index++ + ". ");
                for (int i = 0; i < maxDigits - curDigits; i++) {
                    indexStr.append(" ");
                }
                ui.sendWithSpacer(indexStr.toString() + task);
            }
        } else if (argStr.toLowerCase().startsWith("--date")) {

            String dateStr = argStr.replaceFirst("--date", "").trim();
            if (dateStr.isEmpty()) {
                throw new MissingArgException("On which date? (" + FORMAT_CMD + ")");
            }

            LocalDate date;
            try {
                date = LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (DateTimeParseException exception) {
                throw new CmdFormatException("Sry bro can't understand that date format (" + FORMAT_DATE + ")");
            }

            // Filter out the tasks which are relevant to the given date, along with the actual list indices
            HashMap<Integer, Task> tasks = new HashMap<>();
            int listIndex = 1, lastAddedIndex = 0;
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

            if (tasks.isEmpty()) {
                ui.send("Didn't find anything on " + date.format(DATE_FORMATTER) + " yet, wanna add some stuff? (deadline, event)");
                return;
            }

            ui.send("Here's what we have on " + date.format(DATE_FORMATTER) + ":");

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
        } else {
            throw new CmdFormatException("Uhhh idk waddat (" + FORMAT_CMD + ")");
        }
    }

}
