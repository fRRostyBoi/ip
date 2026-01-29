package notjippity.io;

import java.util.Scanner;

/**
 * Represents the user interface to communicate with the user (e.g. GUI, CLI)
 */
public class Ui {

    private static final String CLI_INPUT_PREFIX = ">> ";
    private static final String CLI_OUTPUT_PREFIX = "[NotJippity] ";
    private static final String CLI_OUTPUT_SPACER = CLI_OUTPUT_PREFIX.replaceAll("\\S", " ");

    private Scanner cliInput = new Scanner(System.in);

    /**
     * Prompts user for a single-line input and returns it
     * @return User's input as a string
     */
    public String getUserInput() {
        System.out.print(CLI_INPUT_PREFIX);
        return cliInput.nextLine();
    }

    /**
     * Sends a message to the CLI prefixed with the bot's name
     * @param message Message to send
     */
    public void send(String message) {
        sendRaw(CLI_OUTPUT_PREFIX + message);
    }

    /**
     * Sends a message to the CLI prefixed with the bot's spacer
     * @param message Message to send
     */
    public void sendWithSpacer(String message) {
        sendRaw(CLI_OUTPUT_SPACER + message);
    }

    /**
     * Sends a message to the CLI on its own, without any appendages or formatting
     * @param message Message to send
     */
    public void sendRaw(String message) {
        System.out.println(message);
    }

    /**
     * Sends a series of messages to the CLI. The first line with be prefixed with the bot name
     * while the subsequent lines will be prefixed with the spacer instead
     * @param messages Messages to send
     */
    public void send(String... messages) {
        boolean isFirst = true;
        for (String message : messages) {
            if (isFirst) {
                send(message);
                isFirst = false;
                continue;
            }

            sendWithSpacer(message);
        }
    }

}
