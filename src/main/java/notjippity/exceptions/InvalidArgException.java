package notjippity.exceptions;

/**
 * Represents an error when user inputs a command with invalid arguments
 */
public class InvalidArgException extends NjException {

    public InvalidArgException(String message) {
        super(message);
    }

}
