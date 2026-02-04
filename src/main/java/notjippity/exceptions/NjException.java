package notjippity.exceptions;

/**
 * Represents an abstract NotJippity exception
 */
public abstract class NjException extends Exception {

    public NjException(String message) {
        super(message);
    }

}
