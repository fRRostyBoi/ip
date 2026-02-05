package notjippity.exceptions;

/**
 * Represents an error when user inputs a command in the wrong format.
 */
public class CmdFormatException extends NjException {

    /**
     * Returns a CmdFormatException instance.
     *
     * @param message The error message.
     */
    public CmdFormatException(String message) {
        super(message);
    }

}
