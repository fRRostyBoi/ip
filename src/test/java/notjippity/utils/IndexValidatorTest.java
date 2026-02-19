package notjippity.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.notes.Note;
import notjippity.notes.NoteTracker;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;
import notjippity.tasks.ToDo;

/**
 * Contains JUnit tests for the IndexValidator class.
 */
public class IndexValidatorTest {

    private TaskTracker taskTracker;
    private NoteTracker noteTracker;

    /**
     * Sets up trackers with test data before each test.
     */
    @BeforeEach
    public void setup() {
        taskTracker = new TaskTracker();
        taskTracker.addTask(new ToDo("Task 1"));
        taskTracker.addTask(new ToDo("Task 2"));
        taskTracker.addTask(new ToDo("Task 3"));

        noteTracker = new NoteTracker();
        noteTracker.addNote(new Note(LocalDate.now(), "Note 1"));
        noteTracker.addNote(new Note(LocalDate.now(), "Note 2"));
    }

    /**
     * Tests that validateNotMissing runs correctly for non-null argument.
     */
    @Test
    public void validateNotMissing_nonNullArg_noException() throws MissingArgException {
        IndexValidator.validateNotMissing("1", "delete");
    }

    /**
     * Tests that validateNotMissing throws MissingArgException for null argument.
     */
    @Test
    public void validateNotMissing_nullArg_throwsMissingArgException() {
        MissingArgException exception = assertThrows(MissingArgException.class, () ->
                IndexValidator.validateNotMissing(null, "delete"));
        assertEquals("Which one? (Format: delete <Id>)", exception.getMessage());
    }

    /**
     * Tests that getValidTask returns correct task for valid indices.
     */
    @Test
    public void getValidTask_validIndices_returnsCorrectTasks() throws CmdFormatException, InvalidArgException {
        Task task1 = IndexValidator.getValidTask("1", taskTracker);
        Task task2 = IndexValidator.getValidTask("2", taskTracker);
        Task task3 = IndexValidator.getValidTask("3", taskTracker);

        assertEquals(taskTracker.getTask(0), task1);
        assertEquals(taskTracker.getTask(1), task2);
        assertEquals(taskTracker.getTask(2), task3);
    }

    /**
     * Tests that getValidTask throws InvalidArgException for various invalid indices.
     */
    @Test
    public void getValidTask_invalidIndices_throwsInvalidArgException() {
        // Non-integer input
        CmdFormatException naiError = assertThrows(CmdFormatException.class, () ->
                IndexValidator.getValidTask("abc", taskTracker));
        assertEquals("Idk waddat, enter the index of the task as seen in the \"list\" command instead",
                naiError.getMessage());

        // Out of bounds
        InvalidArgException oobError = assertThrows(InvalidArgException.class, () ->
                IndexValidator.getValidTask("99", taskTracker));
        assertEquals("Uhhhh we don't have task #99, maybe check with \"list\" again?",
                oobError.getMessage());

        // Zero index
        assertThrows(InvalidArgException.class, () -> IndexValidator.getValidTask("0", taskTracker));

        // Negative index
        assertThrows(InvalidArgException.class, () -> IndexValidator.getValidTask("-1", taskTracker));
    }

    /**
     * Tests that getValidNote returns correct note for valid index.
     */
    @Test
    public void getValidNote_validIndex_returnsCorrectNote() throws CmdFormatException, InvalidArgException {
        Note note1 = IndexValidator.getValidNote("1", noteTracker);
        Note note2 = IndexValidator.getValidNote("2", noteTracker);

        assertEquals(noteTracker.getNote(0), note1);
        assertEquals(noteTracker.getNote(1), note2);
    }

    /**
     * Tests that getValidNote throws the respective exceptions for various invalid indices.
     */
    @Test
    public void getValidNote_invalidIndex_throwsCmdFormatException() {
        // Non-integer input
        CmdFormatException naiError = assertThrows(CmdFormatException.class, () ->
                IndexValidator.getValidNote("abc", noteTracker));
        assertEquals("Idk waddat, enter the index of the note as seen in the \"notes\" command instead",
                naiError.getMessage());

        // Out of bounds
        InvalidArgException oobError = assertThrows(InvalidArgException.class, () ->
                IndexValidator.getValidNote("99", noteTracker));
        assertEquals("Uhhhh we don't have note #99, maybe check with \"notes\" again?",
                oobError.getMessage());

        // Zero index
        assertThrows(InvalidArgException.class, () -> IndexValidator.getValidNote("0", noteTracker));

        // Negative index
        assertThrows(InvalidArgException.class, () -> IndexValidator.getValidNote("-1", noteTracker));
    }

}
