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
import notjippity.commands.NoteCmd;
import notjippity.commands.ToDoCmd;
import notjippity.commands.ToggleCmd;
import notjippity.commands.UndoCmd;
import notjippity.commands.response.CmdOutput;
import notjippity.controllers.MainWindow;
import notjippity.exceptions.FatalNjException;
import notjippity.exceptions.NjException;
import notjippity.exceptions.StorageException;
import notjippity.io.NoteStorage;
import notjippity.io.TaskStorage;
import notjippity.notes.NoteTracker;
import notjippity.tasks.TaskTracker;
import notjippity.utils.UserInputParser;

/**
 * Represents the NotJippity bot and handles all overarching interactions.
 */
public class NotJippity extends Application {

    private MainWindow mainWindow;
    private TaskTracker taskTracker;
    private NoteTracker noteTracker;
    private TaskStorage taskStorage;
    private NoteStorage noteStorage;
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
     * @param stage The stage to assemble to UI onto.
     */
    private void assembleUiOntoStage(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            InputStream windowIconStream = getClass().getResourceAsStream("/images/bot_pfp.png");
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
     * If any errors occur, the bot will terminate immediately.
     */
    private void initBot() {
        taskTracker = new TaskTracker();
        noteTracker = new NoteTracker();
        taskStorage = new TaskStorage();
        noteStorage = new NoteStorage();

        loadStorageData();
        registerCommands();
    }

    /**
     * Loads task and note data from storage.
     * If any errors occur, the bot will terminate immediately.
     */
    private void loadStorageData() {
        try {
            taskStorage.init();
            noteStorage.init();

            taskStorage.loadTasks().forEach(task -> taskTracker.addTask(task));
            noteStorage.loadNotes().forEach(note -> noteTracker.addNote(note));
        } catch (FatalNjException exception) {
            System.out.println(exception.getMessage());
            System.exit(0);
        }
    }

    /**
     * Registers all command handlers.
     */
    private void registerCommands() {
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
        commands.add(new NoteCmd(noteTracker));
    }

    /**
     * Runs the bot's shutdown sequence. Must be called after all logic ends and before bot termination.
     */
    public void shutdown() {
        try {
            taskStorage.saveTasks(taskTracker.getAllDataStrings());
            noteStorage.saveNotes(noteTracker.getAllDataStrings());
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
    public CmdOutput runCmdAndGetResponse(String input) throws NjException {
        String cmdString = UserInputParser.getCommand(input);
        String argString = UserInputParser.getArgString(input);

        Command matchedCommand = findMatchingCommand(cmdString);
        if (matchedCommand != null) {
            return matchedCommand.execute(cmdString, argString);
        }

        return new CmdOutput(true, List.of("Idk what's \"" + cmdString + "\". Typo maybe?"));
    }

    /**
     * Finds a command that matches the given command string.
     *
     * @param cmdString The command string to match.
     * @return The matching command, or null if not found.
     */
    private Command findMatchingCommand(String cmdString) {
        for (Command command : commands) {
            if (command.getCmdName().equalsIgnoreCase(cmdString)) {
                return command;
            }
        }
        return null;
    }

}
