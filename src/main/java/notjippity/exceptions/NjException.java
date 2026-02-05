package notjippity.exceptions;

/**
 * Represents an abstract NotJippity exception.
 */
public abstract class NjException extends Exception {

    /**
     * Returns an Abstract NjException instance.
     *
     * @param message The error message.
     */
    protected NjException(String message) {
        super(message);
    }

}
