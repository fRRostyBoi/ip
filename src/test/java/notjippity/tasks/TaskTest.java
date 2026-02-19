package notjippity.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.StorageException;

/**
 * Contains JUnit tests for the Task class static methods and common functionality.
 */
public class TaskTest {

    /**
     * Tests that isCompleted returns false for a newly created task.
     */
    @Test
    public void isCompleted_newTask_returnsFalse() {
        ToDo task = new ToDo("Test task");
        assertFalse(task.isCompleted());
    }

    /**
     * Tests that complete() sets the task as completed.
     */
    @Test
    public void complete_incompleteTask_returnsTrue() {
        ToDo task = new ToDo("Test task");
        task.complete();
        assertTrue(task.isCompleted());
    }

    /**
     * Tests that undo() sets the task as incomplete.
     */
    @Test
    public void undo_completedTask_returnsFalse() {
        ToDo task = new ToDo("Test task");
        task.complete();
        assertTrue(task.isCompleted());
        task.undo();
        assertFalse(task.isCompleted());
    }

    /**
     * Tests that toggleComplete() toggles the completion status.
     */
    @Test
    public void toggleComplete_incompleteTask_togglesStatus() {
        ToDo task = new ToDo("Test task");
        assertFalse(task.isCompleted());
        task.toggleComplete();
        assertTrue(task.isCompleted());
        task.toggleComplete();
        assertFalse(task.isCompleted());
    }

    /**
     * Tests that matchesKeyword returns true for exact match.
     */
    @Test
    public void matchesKeyword_exactMatch_returnsTrue() {
        ToDo task = new ToDo("Buy groceries");
        assertTrue(task.matchesKeyword("Buy groceries"));
    }

    /**
     * Tests that matchesKeyword returns true for partial match.
     */
    @Test
    public void matchesKeyword_partialMatch_returnsTrue() {
        ToDo task = new ToDo("Buy groceries");
        assertTrue(task.matchesKeyword("grocer"));
        assertTrue(task.matchesKeyword("uy g"));
        assertTrue(task.matchesKeyword("y gro"));
    }

    /**
     * Tests that matchesKeyword is case-insensitive.
     */
    @Test
    public void matchesKeyword_caseInsensitive_returnsTrue() {
        ToDo task = new ToDo("Buy groceries");
        assertTrue(task.matchesKeyword("BUY"));
        assertTrue(task.matchesKeyword("GROCERIES"));
        assertTrue(task.matchesKeyword("gRoCErIEs"));
        assertTrue(task.matchesKeyword("buy"));
    }

    /**
     * Tests that matchesKeyword returns false for non-matching keyword.
     */
    @Test
    public void matchesKeyword_noMatch_returnsFalse() {
        ToDo task = new ToDo("Buy groceries");
        assertFalse(task.matchesKeyword("Ruy broceries"));
    }

    /**
     * Tests that createTaskFromString correctly parses data string types.
     */
    @Test
    public void createTaskFromString_validToDoDataString_returnsToDo() throws StorageException {
        Task todo = Task.createTaskFromString("T||Test task||N");
        assertInstanceOf(ToDo.class, todo);
        assertEquals("T", todo.getTypeIcon());
        assertFalse(todo.isCompleted());

        Task deadline = Task.createTaskFromString("D||Submit report||Y||28/02/2026 1430");
        assertInstanceOf(Deadline.class, deadline);
        assertEquals("D", deadline.getTypeIcon());
        assertTrue(deadline.isCompleted());

        Task event = Task.createTaskFromString("E||Meeting||N||01/03/2026 0900||01/03/2026 1700");
        assertInstanceOf(Event.class, event);
        assertEquals("E", event.getTypeIcon());
        assertFalse(event.isCompleted());
    }

    /**
     * Tests that createTaskFromString handles lowercase type correctly.
     */
    @Test
    public void createTaskFromString_lowercaseType_handlesCorrectly() throws StorageException {
        Task todo = Task.createTaskFromString("t||Test task||N");
        Task deadline = Task.createTaskFromString("d||Test task||N");
        Task event = Task.createTaskFromString("e||Test task||N");

        assertInstanceOf(ToDo.class, todo);
        assertInstanceOf(ToDo.class, deadline);
        assertInstanceOf(ToDo.class, event);
    }

    /**
     * Tests that createTaskFromString throws exception for invalid parts, lengths or types.
     */
    @Test
    public void createTaskFromString_invalidParts_throwsStorageException() {
        // Invalid type
        assertThrows(StorageException.class, () -> Task.createTaskFromString("X||Test task||N"));

        // Valid type but missing other required parts
        assertThrows(StorageException.class, () -> Task.createTaskFromString("T||"));

        // Valid type but extra parts
        assertThrows(StorageException.class, () -> Task.createTaskFromString("T||Two||N||Extra"));
    }

    /**
     * Tests that toString includes correct status indicator for different completion states.
     */
    @Test
    public void toString_variousCompletionStates_showsCorrectIndicator() {
        ToDo task = new ToDo("Test task");
        assertTrue(task.toString().contains("[  ]"));

        task.complete();
        assertTrue(task.toString().contains("[X]"));
    }

}
