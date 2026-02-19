package notjippity.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.StorageException;

/**
 * Contains JUnit tests for the ToDo class.
 */
public class ToDoTest {

    /**
     * Tests that getTypeIcon returns the correct type icon for ToDo tasks.
     */
    @Test
    public void getTypeIcon_normalToDo_returnsTrue() {
        ToDo todo = new ToDo("Test task");
        assertEquals("T", todo.getTypeIcon());

        todo.complete();
        assertEquals("F", todo.getTypeIcon());

        todo.toggleComplete();
        assertEquals("T", todo.getTypeIcon());
    }

    /**
     * Tests that getDataString returns the correct storage format for both incomplete and completed.
     */
    @Test
    public void getDataString_validStatuses_correctFormat() {
        ToDo todo = new ToDo("Test task");
        assertEquals("T||Test task||N", todo.getDataString());

        todo.complete();
        assertEquals("T||Test task||Y", todo.getDataString());
    }

    /**
     * Tests that createTaskFromDataParts correctly parses incomplete ToDo.
     */
    @Test
    public void createTaskFromDataParts_validIncompleteToDo_correctlyParsed() throws StorageException {
        String[] dataParts = {"T", "Test task", "N"};
        ToDo todo = ToDo.createTaskFromDataParts(dataParts);
        assertTrue(todo.toString().contains("Test task"));
        assertFalse(todo.isCompleted());
    }

    /**
     * Tests that createTaskFromDataParts correctly parses completed ToDo.
     */
    @Test
    public void createTaskFromDataParts_validCompletedToDo_correctlyParsed() throws StorageException {
        String[] dataParts = {"T", "Test task", "Y"};
        ToDo todo = ToDo.createTaskFromDataParts(dataParts);
        assertTrue(todo.isCompleted());
    }

    /**
     * Tests that createTaskFromDataParts throws exception for empty, more/less, or incorrect parts.
     */
    @Test
    public void createTaskFromDataParts_invalidParts_throwsStorageException() {
        assertThrows(StorageException.class, () -> ToDo.createTaskFromDataParts(new String[]{"T", "Test task"}));
        assertThrows(StorageException.class, () -> ToDo.createTaskFromDataParts(new String[]{"T", "Test task", "  "}));
        assertThrows(StorageException.class, () -> ToDo.createTaskFromDataParts(new String[]{"  ", "Test task", "  "}));
        assertThrows(StorageException.class, () -> ToDo.createTaskFromDataParts(new String[]{"T", "Test task", "  "}));
        assertThrows(StorageException.class, () -> ToDo.createTaskFromDataParts(new String[]{"X", "Test task", "  "}));
        assertThrows(StorageException.class, () -> ToDo.createTaskFromDataParts(new String[]{"T", "Test task", "AWD"}));
        assertThrows(StorageException.class, () ->
                ToDo.createTaskFromDataParts(new String[]{"T", "Test task", "Y", "Extra", "Extra2"}));
    }

    /**
     * Tests that toString returns the correct format.
     */
    @Test
    public void toString_validToDo_correctFormat() {
        ToDo todo = new ToDo("Test task");
        assertEquals("[  ][T] Test task", todo.toString());
        todo.complete();
        assertEquals("[X][T] Test task", todo.toString());
    }

}
