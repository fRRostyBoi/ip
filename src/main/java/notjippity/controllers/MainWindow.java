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
     * Returns an instance of MainWindow
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
     * Injects the main NotJippity instance into this object
     *
     * @param main The main NotJippity instance
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
        dialogContainer.getChildren().add(DialogBox.createBotDialog("What's up?", botIcon));
    }

    /**
     * Displays the bot's welcome dialog box in the dialog container.
     */
    public void sendExitMsg() {
        dialogContainer.getChildren().add(DialogBox.createBotDialog("Aight cool, cya.", botIcon));
    }

    /**
     * Creates a dialogue box for the user's input followed by one for the bot's response.
     * Clears the user input field.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        assert input != null;

        dialogContainer.getChildren().add(DialogBox.createUserDialog(input, userIcon));

        try {
            List<String> messages = main.runCmdAndGetResponse(input);

            if (!messages.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (String message : messages) {
                    builder.append("\n").append(message);
                }
                String botResponse = builder.toString().replaceFirst("\n", "");

                dialogContainer.getChildren().add(DialogBox.createBotDialog(botResponse, botIcon));
            }
        } catch (NjException exception) {
            dialogContainer.getChildren().add(DialogBox.createBotDialog(exception.getMessage(), botIcon));

            // If it's a fatal error, exit
            if (exception instanceof FatalNjException) {
                System.exit(1);
            }
        }

        userInput.clear();
    }

}
