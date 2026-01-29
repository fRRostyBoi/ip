package notjippity.exceptions;

/**
 * Represents a fatal NotJippity exception. After handling the error,
 * the program should terminate.
 */
public class FatalNJException extends NJException {

    public FatalNJException(String message) {
        super(message);
    }

}
