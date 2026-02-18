package notjippity.controllers;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Controls behaviour for DialogBox.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;
    @FXML
    private Circle displayPictureBg;
    @FXML
    private ImageView displayPicture;

    /**
     * Returns an instance of DialogBox.
     *
     * @param text    The dialogue text.
     * @param img     The user's display picture.
     * @param isError Whether the dialog box is for an error message.
     */
    private DialogBox(String text, Image img, boolean isError) {
        assert text != null;
        assert img != null;

        loadFxml();
        setupText(text, isError);
        setupDisplayPicture(img);
    }

    /**
     * Loads the FXML layout for the dialog box.
     */
    private void loadFxml() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the dialogue text.
     *
     * @param text    The dialogue text to display.
     * @param isError Whether the dialog box is for an error message.
     */
    private void setupText(String text, boolean isError) {
        dialog.setText(text);
        if (isError) {
            dialog.getStyleClass().add("error-label");
        }
    }

    /**
     * Sets up the display picture with the given image.
     *
     * @param img The image to set as the display picture.
     */
    private void setupDisplayPicture(Image img) {
        displayPicture.setImage(img);
        // Clip the ImageView so it's circular
        Circle clipCircle = new Circle(displayPicture.getFitWidth() / 2, displayPicture.getFitHeight() / 2,
                Math.min(displayPicture.getFitWidth(), displayPicture.getFitHeight()) / 2);
        displayPicture.setClip(clipCircle);
    }

    /**
     * Flips the arrangement of child nodes and aligns the DialogBox to the left.
     */
    private void flip() {
        dialog.getStyleClass().add("reply-label");
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Returns a dialog box instance for the user.
     *
     * @param text The dialogue text.
     * @param img  The user's display image.
     * @return The user's dialog box instance.
     */
    public static DialogBox createUserDialog(String text, Image img) {
        return new DialogBox(text, img, false);
    }

    /**
     * Returns a dialog box instance for the bot.
     *
     * @param text    The dialogue text.
     * @param img     The bot's display image.
     * @param isError Whether the dialog box is for an error message.
     * @return The bot's dialog box instance.
     */
    public static DialogBox createBotDialog(String text, Image img, boolean isError) {
        DialogBox db = new DialogBox(text, img, isError);
        db.flip();
        return db;
    }

}
