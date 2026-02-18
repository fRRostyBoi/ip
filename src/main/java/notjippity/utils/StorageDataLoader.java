package notjippity.utils;

import java.util.ArrayList;
import java.util.List;

import notjippity.exceptions.StorageException;

/**
 * Utility class for loading data from storage with error handling.
 */
public class StorageDataLoader {

    /**
     * Loads and parses data strings into objects using the provided parser function.
     *
     * @param dataStrings The list of data strings to parse.
     * @param parser      The function to parse each data string.
     * @param <T>         The type of objects to create.
     * @return The list of parsed objects.
     */
    public static <T> List<T> loadDataWithParser(List<String> dataStrings, DataStringParser<String, T> parser)
            throws StorageException {
        ArrayList<T> items = new ArrayList<>();

        int i = 1;
        try {
            for (String dataString : dataStrings) {
                items.add(parser.parseDataString(dataString));
                i++;
            }
        } catch (StorageException exception) {
            throw new StorageException("Error loading data from file: " + exception.getMessage()
                    + " (line " + i + ")");
        }

        return items;
    }
}

