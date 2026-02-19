package notjippity.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Contains JUnit tests for the TaskTracker class.
 */
public class TaskTrackerTest {

    private TaskTracker taskTracker;

    /**
     * Sets up a fresh TaskTracker before each test.
     */
    @BeforeEach
    public void setUp() {
        taskTracker = new TaskTracker();
    }

    /**
     * Tests that a newly created TaskTracker has size 0.
     */
    @Test
    public void getSize_newTaskTracker_correctSize() {
        assertEquals(0, taskTracker.getSize());

        ToDo task = new ToDo("Test task");

        taskTracker.addTask(task);
        assertEquals(1, taskTracker.getSize());
        taskTracker.addTask(task);
        taskTracker.addTask(task);
        assertEquals(3, taskTracker.getSize());

        taskTracker.removeTask(task);
        assertEquals(2, taskTracker.getSize());
        taskTracker.removeTask(task);
        taskTracker.removeTask(task);
        assertEquals(0, taskTracker.getSize());
    }

    /**
     * Tests that getTask returns the correct task at the specified index.
     */
    @Test
    public void getTask_validIndex_returnsCorrectTask() {
        ToDo task1 = new ToDo("Task 1");
        ToDo task2 = new ToDo("Task 2");
        ToDo task3 = new ToDo("Task 3");
        ToDo task4 = new ToDo("Task 4");
        taskTracker.addTask(task1);
        taskTracker.addTask(task2);
        taskTracker.addTask(task3);
        taskTracker.addTask(task4);
        assertEquals(task1, taskTracker.getTask(0));
        assertEquals(task3, taskTracker.getTask(2));
    }

    /**
     * Tests that getTask throws IndexOutOfBoundsException for invalid indices.
     */
    @Test
    public void getTask_invalidIndices_throwsIndexOutOfBoundsException() {
        taskTracker.addTask(new ToDo("Task 1"));
        assertThrows(IndexOutOfBoundsException.class, () -> taskTracker.getTask(5));
        assertThrows(IndexOutOfBoundsException.class, () -> taskTracker.getTask(-1));
    }

    /**
     * Tests that getTasks returns a cloned list of tasks and does not affect the original list when modified.
     */
    @Test
    public void getTasks_hasTasks_returnsClonedList() {
        ToDo task1 = new ToDo("Task 1");
        ToDo task2 = new ToDo("Task 2");
        taskTracker.addTask(task1);
        taskTracker.addTask(task2);

        List<Task> tasks = taskTracker.getTasks();
        assertEquals(2, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));

        // Verify it's a clone by modifying the returned list. Original should remain unchanged.
        tasks.clear();
        assertEquals(2, taskTracker.getSize());
    }

    /**
     * Tests that getAllDataStrings returns correct data strings for all tasks.
     */
    @Test
    public void getAllDataStrings_withTasks_returnsCorrectDataStrings() {
        taskTracker.addTask(new ToDo("Task 1"));
        taskTracker.addTask(new ToDo("Task 2"));

        List<String> dataStrings = taskTracker.getAllDataStrings();
        assertEquals(2, dataStrings.size());
        assertEquals("T||Task 1||N", dataStrings.get(0));
        assertEquals("T||Task 2||N", dataStrings.get(1));
    }

}
