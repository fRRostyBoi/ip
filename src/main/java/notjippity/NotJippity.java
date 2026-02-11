package notjippity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import notjippity.commands.ByeCmd;
import notjippity.commands.Command;
import notjippity.commands.DeadlineCmd;
import notjippity.commands.DeleteCmd;
import notjippity.commands.DoneCmd;
import notjippity.commands.EventCmd;
import notjippity.commands.FindCmd;
import notjippity.commands.ListCmd;
import notjippity.commands.ToDoCmd;
import notjippity.commands.ToggleCmd;
import notjippity.commands.UndoCmd;
import notjippity.controllers.MainWindow;
import notjippity.exceptions.FatalNjException;
import notjippity.exceptions.NjException;
import notjippity.exceptions.StorageException;
import notjippity.io.Storage;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;
import notjippity.utils.Parser;

/**
 * Represents the NotJippity bot and handles all overarching interactions.
 */
public class NotJippity extends Application {

    private MainWindow mainWindow;
    private TaskTracker taskTracker;
    private Storage storage;
    private final List<Command> commands = new ArrayList<>();

    /**
     * Runs the bot's startup process.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) {
        initBot();
        assembleUiOntoStage(stage);
        stage.show();
    }

    /**
     * Assembles the stage to be ready for showing.
     * If any errors occur, the bot will terminate immediately.
     *
     * @param stage The stage to assemble to UI onto
     */
    private void assembleUiOntoStage(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            InputStream windowIconStream = getClass().getResourceAsStream("/images/window_icon.png");
            assert windowIconStream != null;

            Image windowIcon = new Image(windowIconStream);
            stage.getIcons().add(windowIcon);
            stage.setTitle("NotJippity");

            stage.setScene(scene);

            mainWindow = fxmlLoader.getController();
            assert mainWindow != null;

            mainWindow.setMain(this);
            mainWindow.sendStartupMsg();
        } catch (IOException exception) {
            System.out.println("Error occurred while assembling UI: " + exception.getMessage());
            System.exit(1);
        }
    }

    /**
     * Initialises the bot's controllers. Must be called before performing any further control logic.
     * If any errors occurs, the bot will terminate immediately.
     */
    private void initBot() {
        taskTracker = new TaskTracker();
        storage = new Storage();

        try {
            storage.init();

            storage.loadData().forEach(task -> taskTracker.addTask(task));
        } catch (FatalNjException exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }

        // Register all the command handlers
        commands.add(new ToDoCmd(taskTracker));
        commands.add(new DeadlineCmd(taskTracker));
        commands.add(new EventCmd(taskTracker));
        commands.add(new ListCmd(taskTracker));
        commands.add(new FindCmd(taskTracker));
        commands.add(new ToggleCmd(taskTracker));
        commands.add(new DoneCmd(taskTracker));
        commands.add(new UndoCmd(taskTracker));
        commands.add(new DeleteCmd(taskTracker));
        commands.add(new ByeCmd(this));
    }

    /**
     * Runs the bot's shutdown sequence. Must be called after all logic ends and before bot termination.
     */
    public void shutdown() {
        try {
            storage.saveData(taskTracker.getAllDataStrings());
        } catch (StorageException exception) {
            System.out.println(exception.getMessage());
        }

        mainWindow.sendExitMsg();
        System.exit(0);
    }

    /**
     * Runs a command based on the given input and returns the response.
     *
     * @param input The raw user input.
     * @return The response from running the command.
     * @throws NjException If running the command returns an error.
     */
    public List<String> runCmdAndGetResponse(String input) throws NjException {
        String cmdString = Parser.getCommand(input);
        String argString = Parser.getArgString(input);

        // Find and run command based on user input
        for (Command command : commands) {
            if (command.getCmdName().equalsIgnoreCase(cmdString)) {
                return command.execute(cmdString, argString);
            }
        }

        // If a match isn't found, send an error message
        return List.of("Idk what's \"" + cmdString + "\". Typo maybe?");
    }

}
