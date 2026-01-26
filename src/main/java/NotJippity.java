import exceptions.CmdFormatException;
import exceptions.MissingArgException;
import exceptions.NJException;
import exceptions.UnknownCmdException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.util.ArrayList;
import java.util.Scanner;

public class NotJippity {

    private static final String msgPrefix = "[NotJippity]";
    private static final String msgPrefixSpacer = msgPrefix.replaceAll("\\S", " ");
    private static final ArrayList<Task> tasklist = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printStartupMsg();

        doMainLoop(scanner);

        printExitMsg();
    }

    /**
     * Activates the bot's main logic. Loops infinitely to handle inputs, terminating when the user types "bye"
     * @param scanner Scanner for user inputs
     */
    private static void doMainLoop(Scanner scanner) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.print("\n>> ");
            String input = scanner.nextLine();
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
                    case "bye":
                        isRunning = false;
                        continue;
                    // If none of the commands match
                    default:
                        throw new UnknownCmdException("Idk what's \"" + command + "\". Typo maybe?");
                }
            } catch (NJException exception) {
                // Default printing out the error message, which should be provided in detail when thrown
                System.out.println(msgPrefix + " " + exception.getMessage());
            }
        }
    }

    /**
     * Adds a ToDo task into the tasklist and executes feedback
     * @param argString User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     */
    private static void addToDo(String argString) throws MissingArgException {
        // If the task name is empty
        if (argString.isEmpty()) throw new MissingArgException("Sooo... what's this task called? (todo <Name>)");

        Task task = new ToDo(argString);
        tasklist.add(task);
        System.out.println(msgPrefix + " ++ " + task + " (" + tasklist.size() + " total)");
    }

    /**
     * Adds a Deadline task into the tasklist and executes feedback
     * @param argString User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     */
    private static void addDeadline(String argString) throws MissingArgException {
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
        System.out.println(msgPrefix + " ++ " + task + " (" + tasklist.size() + " total)");
    }

    /**
     * Adds an Event task into the tasklist and executes feedback
     * @param argString User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     * @throws CmdFormatException If flags are in the wrong order
     */
    private static void addEvent(String argString) throws MissingArgException, CmdFormatException {
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
        System.out.println(msgPrefix + " ++ " + task + " (" + tasklist.size() + " tasks)");
    }

    /**
     * Prints the list of all tasks currently stored
     */
    private static void listTasks() {
        System.out.println(msgPrefix + " Here's what we have so far:");

        int maxDigits = 1 + (int) Math.floor(Math.log10(tasklist.size()));

        int index = 1;
        for (Task task : tasklist) {
            int curDigits = 1 + (int) Math.floor(Math.log10(index));
            StringBuilder indexStr = new StringBuilder(index++ + ". ");
            for (int i = 0; i < maxDigits - curDigits; i++) indexStr.append(" ");
            System.out.println(msgPrefixSpacer + " " + indexStr + task);
        }
    }

    /**
     * Completes a task and executes feedback
     */
    private static void completeTask(Task task) {
        task.complete();
        System.out.println(msgPrefix + " " + task);
    }

    /**
     * Undo a task and executes feedback
     */
    private static void undoTask(Task task) {
        task.undo();
        System.out.println(msgPrefix + " " + task);
    }

    /**
     * Toggles a task's completion status and executes feedback
     */
    private static void toggleTaskCompletion(Task task) {
        task.toggleComplete();
        System.out.println(msgPrefix + " " + task);
    }

    /**
     * Prints the same message provided as argument
     * @param message Message to make the bot echo
     */
    private static void echoMsg(String message) {
        System.out.println(msgPrefix + " " + message);
    }

    /**
     * Prints the startup message
     */
    private static void printStartupMsg() {
        System.out.println("____________________________________________________________");
        System.out.println(msgPrefix + " What's up?");
    }

    /**
     * Prints the shutdown message
     */
    private static void printExitMsg() {
        System.out.println(msgPrefix + " Aite cool, cya.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Returns the task in the tasklist with the index given in the command arguments.
     * Prints an error message if the argument string isn't of the correct format, or out of bounds of the tasklist
     * @param indexArg The user's full command arguments. It should contain only 1 positive integer within the tasklist's size, with index 1 corresponding to the first task
     * @return The task given by the provided index, or null if it doesn't exist
     */
    private static Task getTaskByArg(String indexArg) {
        try {
            return tasklist.get(Integer.parseInt(indexArg) - 1);
        } catch (NumberFormatException exception) {
            System.out.println("Wrong format bro, enter the index of the task as seen in the \"list\" command");
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("Uhhhh wrong index mate, typo maybe?");
        }
        return null;
    }

}
