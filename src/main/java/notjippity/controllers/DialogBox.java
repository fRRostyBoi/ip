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

/**
 * Controls behaviour for DialogBox.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Returns an instance of DialogBox.
     *
     * @param text The dialogue text.
     * @param img  The user's display picture.
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the arrangement of child nodes and aligns the DialogBox to the left.
     */
    private void flip() {
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
        return new DialogBox(text, img);
    }

    /**
     * Returns a dialog box instance for the bot.
     *
     * @param text The dialogue text.
     * @param img  The bot's display image.
     * @return The bot's dialog box instance.
     */
    public static DialogBox createBotDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        return db;
    }

}
