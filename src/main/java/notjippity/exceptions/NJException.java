package notjippity.exceptions;

/**
 * Represents an abstract NotJippity exception
 */
public abstract class NJException extends Exception {

    public NJException(String message) {
        super(message);
    }

}
