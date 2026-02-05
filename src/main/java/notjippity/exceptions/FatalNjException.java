package notjippity.exceptions;

/**
 * Represents a fatal NotJippity exception. After handling the error, the program should terminate.
 */
public class FatalNjException extends NjException {

    /**
     * Returns a FatalNjException instance.
     *
     * @param message The error message.
     */
    public FatalNjException(String message) {
        super(message);
    }

}
