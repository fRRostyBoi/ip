import exceptions.CmdFormatException;
import exceptions.MissingArgException;
import exceptions.NJException;
import exceptions.UnknownCmdException;
import file.Ui;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.util.ArrayList;

public class NotJippity {
    
    private Ui ui;
    private static final ArrayList<Task> tasklist = new ArrayList<>();
    
    public static void main(String[] args) {
        NotJippity bot = new NotJippity();
        bot.printStartupMsg();
        bot.doMainLoop();
        bot.printExitMsg();
    }

    /**
     * Activates the bot's main logic. Loops infinitely to handle inputs, terminating when the user types "bye"
     */
    private void doMainLoop() {
        boolean isRunning = true;
        while (isRunning) {
            String input = ui.getUserInput();
            String command = input.trim().split(" ")[0].toLowerCase();
            String argString = input.trim().replaceFirst(command, "").trim();

            // Terminate if the user types "bye" in any case combination, otherwise just echoes
            try {
                switch (command) {
                    case "todo":
                        addToDo(argString);
                        break;
                    case "deadline":
                        addDeadline(argString);
                        break;
                    case "event":
                        addEvent(argString);
                        break;
                    case "list":
                        listTasks();
                        break;
                    case "done": {
                        Task task = getTaskByArg(argString);
                        if (task != null) completeTask(task);
                    }
                        break;
                    case "undo": {
                        Task task = getTaskByArg(argString);
                        if (task != null) undoTask(task);
                    }
                        break;
                    case "toggle": {
                        Task task = getTaskByArg(argString);
                        if (task != null) toggleTaskCompletion(task);
                    }
                        break;
                    case "delete": {
                        Task task = getTaskByArg(argString);
                        if (task != null) deleteTask(task);
                    }
                        break;
                    case "bye":
                        isRunning = false;
                        continue;
                    // If none of the commands match
                    default:
                        throw new UnknownCmdException("Idk what's \"" + command + "\". Typo maybe?");
                }
            } catch (NJException exception) {
                // Default printing out the error message, which should be provided in detail when thrown
                ui.send(exception.getMessage());
            }
        }
    }

    /**
     * Adds a ToDo task into the tasklist and executes feedback
     * @param argString User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     */
    private void addToDo(String argString) throws MissingArgException {
        // If the task name is empty
        if (argString.isEmpty()) throw new MissingArgException("Sooo... what's this task called? (todo <Name>)");

        Task task = new ToDo(argString);
        tasklist.add(task);
        ui.send("++ " + task + " (" + tasklist.size() + " total)");
    }

    /**
     * Adds a Deadline task into the tasklist and executes feedback
     * @param argString User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     */
    private void addDeadline(String argString) throws MissingArgException {
        String formatStr = "Format: /deadline <Name> --by <Deadline>";
        String argStringLow = argString.trim().toLowerCase();

        // If the user input something like "deadline" or "deadline --by [...]"
        if (argStringLow.isEmpty() || argStringLow.startsWith("--by")) throw new MissingArgException("First things first, what's this task called? (" + formatStr + ")");

        // If the user input doesn't contain "--by"
        if (!argStringLow.contains("--by")) throw new MissingArgException("Np but tell me when it's to be done by (" + formatStr + ")");

        String[] argSets = argString.trim().split("--by");
        // If the user input doesn't specify the date after "--by"
        if (argSets.length == 1) throw new MissingArgException("Didja forget to put something at the back of --by? (" + formatStr + ")");

        String taskName = argSets[0].trim(),
               byDate = argSets[1].trim();

        Task task = new Deadline(taskName, byDate);
        tasklist.add(task);
        ui.send("++ " + task + " (" + tasklist.size() + " total)");
    }

    /**
     * Adds an Event task into the tasklist and executes feedback
     * @param argString User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     * @throws CmdFormatException If flags are in the wrong order
     */
    private void addEvent(String argString) throws MissingArgException, CmdFormatException {
        String formatStr = "Format: /event <Name> --from <From> --to <To>";
        String argStringLow = argString.trim().toLowerCase();

        // If the user input something like "event", "event --from [...]" or "event --to [...]"
        if (argStringLow.isEmpty() || argStringLow.startsWith("--from") || argStringLow.startsWith("--to")) throw new MissingArgException("First things first, what's this task called? (" + formatStr + ")");

        // If the user input doesn't contain "--from" or "--to"
        if (!argStringLow.contains("--from") || !argStringLow.contains("--to")) throw new MissingArgException("Np but tell me when it's to be done by (" + formatStr + ")");

        // If the user input has "--to" preceding "--from"
        if (argStringLow.indexOf("--from") > argStringLow.indexOf("--to")) throw new CmdFormatException("Write --from before --to pls (" + formatStr + ")");

        String[] exclNameArgs = argString.trim().split("--from");
        String taskName = exclNameArgs[0].trim(), exclFromArgs = exclNameArgs[1].trim();
        String[] exclToArgs = exclFromArgs.split("--to");
        String fromStr = exclToArgs[0].trim(), toStr = exclToArgs[1].trim();

        // If the arguments following --from or --to is empty
        if (fromStr.isEmpty() || toStr.isEmpty()) throw new MissingArgException("Didja forget to put something at the back of --from or --to? (" + formatStr + ")");

        Task task = new Event(taskName, fromStr, toStr);
        tasklist.add(task);
        ui.send("++ " + task + " (" + tasklist.size() + " tasks)");
    }

    /**
     * Prints the list of all tasks currently stored
     */
    private void listTasks() {
        if (tasklist.isEmpty()) {
            ui.send("Nothing here yet man, wanna add some stuff? (todo, deadline, event)");
            return;
        }

        ui.send("Here's what we have so far:");

        int maxDigits = 1 + (int) Math.floor(Math.log10(tasklist.size()));

        int index = 1;
        for (Task task : tasklist) {
            int curDigits = 1 + (int) Math.floor(Math.log10(index));
            StringBuilder indexStr = new StringBuilder(index++ + ". ");
            for (int i = 0; i < maxDigits - curDigits; i++) indexStr.append(" ");
            ui.sendWithSpacer(indexStr.toString() + task);
        }
    }

    /**
     * Completes a task and executes feedback
     */
    private void completeTask(Task task) {
        task.complete();
        ui.send(task.toString());
    }

    /**
     * Undo a task and executes feedback
     */
    private void undoTask(Task task) {
        task.undo();
        ui.send(task.toString());
    }

    /**
     * Toggles a task's completion status and executes feedback
     * @param task The selected task
     */
    private void toggleTaskCompletion(Task task) {
        task.toggleComplete();
        ui.send(task.toString());
    }

    /**
     * Deletes a task
     * @param task The selected task
     */
    private void deleteTask(Task task) {
        tasklist.remove(task);
        ui.send("-- " + task + " (" + tasklist.size() + " total)");
    }

    /**
     * Prints the same message provided as argument
     * @param message Message to make the bot echo
     */
    private void echoMsg(String message) {
        ui.send(message);
    }

    /**
     * Prints the startup message
     */
    private void printStartupMsg() {
        ui.send("____________________________________________________________");
        ui.sendWithSpacer("What's up?");
    }

    /**
     * Prints the shutdown message
     */
    private void printExitMsg() {
        ui.send("Aite cool, cya.");
        ui.sendWithSpacer("____________________________________________________________");
    }

    /**
     * Returns the task in the tasklist with the index given in the command arguments.
     * Prints an error message if the argument string isn't of the correct format, or out of bounds of the tasklist
     * @param indexArg The user's full command arguments. It should contain only 1 positive integer within the tasklist's size, with index 1 corresponding to the first task
     * @return The task given by the provided index, or null if it doesn't exist
     */
    private Task getTaskByArg(String indexArg) {
        try {
            return tasklist.get(Integer.parseInt(indexArg) - 1);
        } catch (NumberFormatException exception) {
            ui.send("Wrong format bro, enter the index of the task as seen in the \"list\" command");
        } catch (IndexOutOfBoundsException exception) {
            ui.send("Uhhhh we don't have task #" + indexArg + ", maybe check with \"list\" again?");
        }
        return null;
    }

}
