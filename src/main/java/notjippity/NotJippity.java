package notjippity;

import notjippity.commands.ByeCmd;
import notjippity.commands.Command;
import notjippity.commands.DeadlineCmd;
import notjippity.commands.DeleteCmd;
import notjippity.commands.DoneCmd;
import notjippity.commands.EventCmd;
import notjippity.commands.ListCmd;
import notjippity.commands.ToDoCmd;
import notjippity.commands.ToggleCmd;
import notjippity.commands.UndoCmd;
import notjippity.utils.Parser;
import notjippity.io.Storage;
import notjippity.tasks.TaskTracker;
import notjippity.io.Ui;
import notjippity.exceptions.FatalNJException;
import notjippity.exceptions.NJException;
import notjippity.exceptions.StorageException;
import notjippity.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class NotJippity {

    private Ui ui;
    private TaskTracker taskTracker;
    private Storage storage;
    private final List<Command> commands = new ArrayList<>();

    private boolean isRunning = true;

    public static void main(String[] args) {
        NotJippity bot = new NotJippity();
        bot.init();
        bot.startMainLoop();
        bot.shutdown();
    }

    /**
     * Performs the bot's startup sequence, sending the welcome message when done.
     * This method must be called at the start of the program, before any methods.
     * If any initialisation error occurs, the bot will terminate immediately.
     */
    private void init() {
        ui = new Ui();
        taskTracker = new TaskTracker();
        storage = new Storage(ui);
        storage.init();

        try {
            for (Task task : storage.loadData()) {
                taskTracker.addTask(task);
            }
        } catch (StorageException exception) {
            ui.send(exception.getMessage());
            System.exit(1);
        }

        // Register all the command handlers
        commands.add(new ToDoCmd(ui, taskTracker));
        commands.add(new DeadlineCmd(ui, taskTracker));
        commands.add(new EventCmd(ui, taskTracker));
        commands.add(new ListCmd(ui, taskTracker));
        commands.add(new ToggleCmd(ui, taskTracker));
        commands.add(new DoneCmd(ui, taskTracker));
        commands.add(new UndoCmd(ui, taskTracker));
        commands.add(new DeleteCmd(ui, taskTracker));
        commands.add(new ByeCmd(this));

        // Startup complete, send the welcome message
        printStartupMsg();
    }

    /**
     * Performs the bot's shutdown sequence, sending the exit message when done.
     * This method must be called at the end of the program, after all methods.
     */
    private void shutdown() {
        try {
            storage.saveData(taskTracker.getAllDataStrings());
        } catch (StorageException exception) {
            ui.send(exception.getMessage());
        }

        printExitMsg();
    }

    /**
     * Activates the bot's main logic. Loops infinitely to handle inputs, terminating when the user types "bye"
     */
    private void startMainLoop() {
        while (isRunning) {
            String input = ui.getUserInput();
            String cmdString = Parser.getCommand(input);
            String argString = Parser.getArgString(input);

            // Try to match the given command
            boolean match = false;
            for (Command command : commands) {
                if (command.getName().equalsIgnoreCase(cmdString)) {
                    match = true;
                    try {
                        command.execute(cmdString, argString);
                    } catch (NJException exception) {
                        ui.send(exception.getMessage());
                        if (exception instanceof FatalNJException) {
                            System.exit(1);
                        }
                    }
                    break;
                }
            }

            // If a match isn't found, send an error message
            if (!match) {
//                throw new UnknownCmdException("Idk what's \"" + cmdString + "\". Typo maybe?");
                ui.send("Idk what's \"" + cmdString + "\". Typo maybe?");
            }
        }
    }

    /**
     * Prints the same message provided as argument
     *
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
     * Stops the main loop of the bot
     */
    public void stopMainLoop() {
        isRunning = false;
    }

}
