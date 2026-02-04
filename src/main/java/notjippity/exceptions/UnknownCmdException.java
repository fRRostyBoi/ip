package notjippity.exceptions;

/**
 * Represents an error when user inputs an unknown command
 */
public class UnknownCmdException extends NjException {

    public UnknownCmdException(String message) {
        super(message);
    }

}
