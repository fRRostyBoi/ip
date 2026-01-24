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
                case "undo":{
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
                default:
                    echoMsg(input);
            }
        }
    }

    /**
     * Adds a ToDo task into the tasklist and executes feedback
     * Executes error feedback if the task name is empty
     * @param argString User's input command arguments
     */
    private static void addToDo(String argString) {
        // If the task name is empty
        if (argString.isEmpty()) {
            System.out.println(msgPrefix + " What am I supposed to call this task bruh, specify a name");
            return;
        }

        Task task = new ToDo(argString);
        tasklist.add(task);
        System.out.println(msgPrefix + " ++ " + task + " (" + tasklist.size() + " total)");
    }

    /**
     * Adds a Deadline task into the tasklist and executes feedback.
     * Executes error feedback if the task name is empty
     * User input must follow the "deadline --by {@literal <}date{@literal >}" format, failing which an error feedback will be executed
     * @param argString User's input command arguments
     */
    private static void addDeadline(String argString) {
        String[] argSets = argString.split("--by");
        // If the user didn't include --by
        if (argSets.length == 1) {
            System.out.println(msgPrefix + " Gimme a deadline date using \"--by <date>\" too man");
            return;
        }

        String taskName = argSets[0].trim(),
               byDate = argSets[1].trim();

        // If the task name or the argument following --by is empty
        if (taskName.isEmpty()) {
            System.out.println(msgPrefix + " What am I supposed to call this task bruh, specify a name");
            return;
        }
        if (byDate.isEmpty()) {
            System.out.println(msgPrefix + " Gimme a deadline date using \"--by <date>\" too man");
            return;
        }

        Task task = new Deadline(taskName, byDate);
        tasklist.add(task);
        System.out.println(msgPrefix + " ++ " + task + " (" + tasklist.size() + " total)");
    }

    /**
     * Adds an Event task into the tasklist and executes feedback.
     * Executes error feedback if the task name is empty
     * User input must follow the "deadline --from {@literal <}date{@literal >} --to {@literal <}date{@literal >}" format, failing which an error feedback will be executed
     * @param argString User's input command arguments
     */
    private static void addEvent(String argString) {
        String fromArgErrorMsg = " Sooo when's it from? (add \"--from <date>\" too pls)",
               toArgErrorMsg = " ... and when's it until? (remem to append \"--to <date>\")";

        String[] argSets = argString.split("--from");
        // If the user didn't include --from
        if (argSets.length == 1) {
            System.out.println(msgPrefix + fromArgErrorMsg);
            return;
        }

        String taskName = argSets[0].trim(),
               backArgs = argSets[1].trim();
        // If the task name is empty
        if (taskName.isEmpty()) {
            System.out.println(msgPrefix + " What am I supposed to call this task bruh, specify a name");
            return;
        }

        String[] fromToArgSets = backArgs.split("--to");
        // If the user didn't include --to, or if did something like --from --to which will have 0 length after splitting
        if (fromToArgSets.length <= 1) {
            System.out.println(msgPrefix + toArgErrorMsg);
            return;
        }

        String fromDate = fromToArgSets[0].trim(),
               toDate = fromToArgSets[1].trim();

        // If the arguments following --from or --to is empty
        if (fromDate.isEmpty()) {
            System.out.println(msgPrefix + fromArgErrorMsg);
            return;
        }
        if (toDate.isEmpty()) {
            System.out.println(msgPrefix + toArgErrorMsg);
            return;
        }

        Task task = new Event(taskName, fromDate, toDate);
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
