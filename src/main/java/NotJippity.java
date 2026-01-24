import tasks.Task;

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
                case "add":
                    addTask(argString);
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
     * Adds a task into the tasklist and executes feedback
     * @param taskString The task to add into the tasklist
     */
    private static void addTask(String taskString) {
        tasklist.add(new Task(taskString));
        System.out.println(msgPrefix + " Task added: " + taskString);
    }

    /**
     * Prints the list of all tasks currently stored
     */
    private static void listTasks() {
        System.out.println(msgPrefix + " Here's what we have so far:");

        int index = 1;
        for (Task task : tasklist) System.out.println(msgPrefixSpacer + " " + index++ + ". " + task);
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
