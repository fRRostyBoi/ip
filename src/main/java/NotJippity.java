import java.util.ArrayList;
import java.util.Scanner;

public class NotJippity {

    private static final String msgPrefix = "[NotJippity]";
    private static final String msgPrefixSpacer = msgPrefix.replaceAll("\\S", " ");
    private static final ArrayList<String> tasklist = new ArrayList<>();

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
                case "bye":
                    isRunning = false;
                    continue;
                default:
                    echoMsg(input);
            }
        }
    }

    /**
     * Adds a task into the tasklist and prints feedback
     * @param task The task to add into the tasklist
     */
    private static void addTask(String task) {
        tasklist.add(task);
        System.out.println(msgPrefix + " Task added: " + task);
    }

    /**
     * Prints the list of all tasks currently stored
     */
    private static void listTasks() {
        System.out.println(msgPrefix + " Here's what we have so far:");

        int index = 1;
        for (String task : tasklist) System.out.println(msgPrefixSpacer + " " + index++ + ". " + task);
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

}
