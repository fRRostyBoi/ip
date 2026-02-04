package notjippity.exceptions;

/**
 * Represents an error when user inputs a command in the wrong format
 */
public class CmdFormatException extends NjException {

    public CmdFormatException(String message) {
        super(message);
    }

}
