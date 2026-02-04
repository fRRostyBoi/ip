package notjippity.exceptions;

/**
 * Represents an error when user inputs a command with missing arguments
 */
public class MissingArgException extends NjException {

    public MissingArgException(String message) {
        super(message);
    }

}
