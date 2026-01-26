package exceptions;

/**
 * Represents an error when user inputs a command with missing arguments
 */
public class MissingArgException extends NJException {

    public MissingArgException(String message) {
        super(message);
    }

}
