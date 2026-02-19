package notjippity.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.StorageException;

/**
 * Contains JUnit tests for the StorageDataLoader class.
 */
public class StorageDataLoaderTest {

    /**
     * Tests that loadDataWithParser correctly parses valid data strings with the provided parser.
     */
    @Test
    public void loadDataWithParser_validData_returnsParsedList() throws StorageException {
        List<String> dataStrings = List.of("item1", "item2", "item3");
        List<String> resultNormal = StorageDataLoader.loadDataWithParser(dataStrings, String::toUpperCase);

        assertEquals(3, resultNormal.size());
        assertEquals("ITEM1", resultNormal.get(0));
        assertEquals("ITEM2", resultNormal.get(1));
        assertEquals("ITEM3", resultNormal.get(2));

        List<String> resultEmpty = StorageDataLoader.loadDataWithParser(List.of(), s -> null);
        assertTrue(resultEmpty.isEmpty());
    }

    /**
     * Tests that loadDataWithParser throws StorageException with correct line number on parse error.
     */
    @Test
    public void loadDataWithParser_parseError_throwsExceptionWithLineNumber() {
        // Parser that throws exception for "invalid"
        DataStringParser<String, String> parser = s -> {
            if (s.equals("invalid")) {
                throw new StorageException("Invalid data");
            }
            return s;
        };

        // Error on line 1
        List<String> listFirstError = List.of("invalid", "valid");
        StorageException firstLineError = assertThrows(StorageException.class, () ->
                StorageDataLoader.loadDataWithParser(listFirstError, parser));
        assertTrue(firstLineError.getMessage().contains("line 1"));

        // Error on line 2
        List<String> listSecondError = List.of("valid", "invalid", "valid");
        StorageException secondLineError = assertThrows(StorageException.class, () ->
                StorageDataLoader.loadDataWithParser(listSecondError, parser));
        assertTrue(secondLineError.getMessage().contains("line 2"));
        assertTrue(secondLineError.getMessage().contains("Invalid data"));
    }

}
