package notjippity.controllers;

import java.io.InputStream;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import notjippity.NotJippity;
import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.FatalNjException;
import notjippity.exceptions.NjException;

/**
 * Controls behaviour for MainWindow.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private NotJippity main;

    private final Image userIcon;
    private final Image botIcon;

    /**
     * Returns an instance of MainWindow.
     */
    public MainWindow() {
        InputStream userIconStream = getClass().getResourceAsStream("/images/user_pfp.png");
        InputStream botIconStream = getClass().getResourceAsStream("/images/bot_pfp.png");

        assert userIconStream != null;
        assert botIconStream != null;

        userIcon = new Image(userIconStream);
        botIcon = new Image(botIconStream);
    }

    /**
     * Injects the main NotJippity instance into this object.
     *
     * @param main The main NotJippity instance.
     */
    public void setMain(NotJippity main) {
        this.main = main;
        assert this.main != null;
    }

    /**
     * Initialises the Main Window. Automatically called by JavaFX.
     */
    @FXML
    public void initialize() {
        assert scrollPane != null;
        assert dialogContainer != null;
        assert userInput != null;
        assert sendButton != null;

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Displays the bot's welcome dialog box in the dialog container.
     */
    public void sendStartupMsg() {
        dialogContainer.getChildren().add(DialogBox.createBotDialog("What's up?", botIcon, false));
    }

    /**
     * Displays the bot's welcome dialog box in the dialog container.
     */
    public void sendExitMsg() {
        dialogContainer.getChildren().add(DialogBox.createBotDialog("Aight cool, cya.", botIcon, false));
    }

    /**
     * Creates a dialogue box for the user's input followed by one for the bot's response.
     * Clears the user input field.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        assert input != null;

        userInput.clear();
        addUserDialogBox(input);

        CmdOutput output = getResponseMessages(input);
        if (output != null) {
            String botReply = formatBotReply(output.getReply());
            addBotDialogBox(botReply, output.isError());
        }
    }

    /**
     * Gets response messages from the bot, handling any exceptions.
     *
     * @param input The user input.
     * @return The bot's response, or null if a fatal exception occurred.
     */
    private CmdOutput getResponseMessages(String input) {
        try {
            return main.runCmdAndGetResponse(input);
        } catch (FatalNjException exception) {
            handleFatalException(exception);
            return null;
        } catch (NjException exception) {
            return new CmdOutput(true, List.of(exception.getMessage()));
        }
    }

    /**
     * Handles fatal exceptions by printing and exiting.
     *
     * @param exception The fatal exception.
     */
    private void handleFatalException(FatalNjException exception) {
        System.out.println(exception.getMessage());
        System.exit(1);
    }

    /**
     * Constructs the bot's reply from the list of response messages.
     *
     * @param messages The list of response messages from NotJippity.runCmdAndGetResponse.
     * @return The reply as a single string, meant for UI.
     */
    private String formatBotReply(List<String> messages) {
        return String.join("\n", messages);
    }

    /**
     * Adds a user dialogue box to the window.
     *
     * @param text The text to display.
     */
    private void addUserDialogBox(String text) {
        dialogContainer.getChildren().add(DialogBox.createUserDialog(text, userIcon));
    }

    /**
     * Adds a bot dialogue box to the window.
     *
     * @param text    The text to display.
     * @param isError Whether the dialog box is for an error message.
     */
    private void addBotDialogBox(String text, boolean isError) {
        dialogContainer.getChildren().add(DialogBox.createBotDialog(text, botIcon, isError));
    }

}
