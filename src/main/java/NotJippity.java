import java.util.Scanner;

public class NotJippity {

    private static final String msgPrefix = "[NotJippity]";

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

            // Terminate if the user types "bye" in any case combination, otherwise just echoes
            switch (input.trim().toLowerCase()) {
                case "bye":
                    isRunning = false;
                    continue;
                default:
                    echoMsg(input);
            }
        }
    }

    /**
     * Makes the bot reply with the same message provided as argument
     * @param message Message to make the bot echo
     */
    private static void echoMsg(String message) {
        System.out.println(msgPrefix + " " + message);
    }

    /**
     * Prints the bot's startup message
     */
    private static void printStartupMsg() {
        System.out.println("____________________________________________________________");
        System.out.println(msgPrefix + " What's up?");
    }

    /**
     * Prints the bot's shutdown message
     */
    private static void printExitMsg() {
        System.out.println(msgPrefix + " Aite cool, cya.");
        System.out.println("____________________________________________________________");
    }

}
