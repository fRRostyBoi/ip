package notjippity.exceptions;

/**
 * Represents an error when user inputs a command with invalid arguments.
 */
public class InvalidArgException extends NjException {

    /**
     * Returns an InvalidArgException instance.
     *
     * @param message The error message.
     */
    public InvalidArgException(String message) {
        super(message);
    }

}
