package notjippity.utils;

import notjippity.exceptions.StorageException;

/**
 * Represents a function that parses a data string into an object of the appropriate type.
 */
@FunctionalInterface
public interface DataStringParser<String, T> {

    /**
     * Parses the given data string into an object of the appropriate type.
     *
     * @param dataString The data string to parse.
     * @return The parsed object of type T.
     * @throws StorageException If the data string is of the wrong format/corrupted.
     */
    T parseDataString(String dataString) throws StorageException;

}
