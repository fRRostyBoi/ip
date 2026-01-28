package exceptions;

/**
 * Represents an exception relating to the Storage aspect of the bot
 * (e.g. Unable to create file due to permissions, corrupted file)
 */
public class StorageException extends NJException {

    public StorageException(String message) {
        super(message);
    }

}
