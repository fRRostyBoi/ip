package notjippity.exceptions;

/**
 * Represents an error when user inputs a command with missing arguments.
 */
public class MissingArgException extends NjException {

    /**
     * Returns a MissingArgException instance.
     *
     * @param message The error message.
     */
    public MissingArgException(String message) {
        super(message);
    }

}
