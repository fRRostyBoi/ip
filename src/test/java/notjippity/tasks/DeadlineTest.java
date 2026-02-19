package notjippity.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.StorageException;

/**
 * Contains JUnit tests for the Deadline class.
 */
public class DeadlineTest {

    /**
     * Tests that getTypeIcon returns the correct type icon for Deadline tasks.
     */
    @Test
    public void getTypeIcon_normalDeadline_returnsCorrectIcon() {
        LocalDateTime byDate = LocalDateTime.of(2026, 3, 15, 10, 0);
        Deadline deadline = new Deadline("Test deadline", byDate);
        assertEquals("D", deadline.getTypeIcon());
    }

    /**
     * Tests that getDataString returns the correct storage format for both incomplete and completed.
     */
    @Test
    public void getDataString_bothStatuses_correctFormat() {
        LocalDateTime byDate = LocalDateTime.of(2026, 4, 20, 9, 0);
        Deadline deadline = new Deadline("Test task", byDate);
        assertEquals("D||Test task||N||20/04/2026 0900", deadline.getDataString());

        deadline.complete();
        assertEquals("D||Test task||Y||20/04/2026 0900", deadline.getDataString());
    }

    /**
     * Tests that hasDate returns true for matching date.
     */
    @Test
    public void hasDate_matchingDate_returnsTrue() {
        LocalDateTime byDate = LocalDateTime.of(2026, 6, 15, 12, 0);
        Deadline deadline = new Deadline("Test task", byDate);
        assertTrue(deadline.hasDate(LocalDate.of(2026, 6, 15)));
    }

    /**
     * Tests that hasDate returns false for non-matching date.
     */
    @Test
    public void hasDate_differentDate_returnsFalse() {
        LocalDateTime byDate = LocalDateTime.of(2026, 6, 15, 12, 0);
        Deadline deadline = new Deadline("Test task", byDate);
        assertFalse(deadline.hasDate(LocalDate.of(2026, 6, 16)));
    }

    /**
     * Tests that createTaskFromDataParts correctly parses valid data.
     */
    @Test
    public void createTaskFromDataParts_validFormat_correctlyParsed() throws StorageException {
        String[] dataParts = {"D", "Submit report", "N", "28/02/2026 1430"};
        Deadline deadline = Deadline.createTaskFromDataParts(dataParts);
        assertTrue(deadline.toString().contains("28/02/2026 1430"));
        assertTrue(deadline.toString().contains("Submit report"));
        assertFalse(deadline.isCompleted());
        assertTrue(deadline.hasDate(LocalDate.of(2026, 2, 28)));

        Deadline deadlineYes = Deadline.createTaskFromDataParts(
                new String[]{"D", "Submit report", "Y", "28/02/2026 1430"});
        assertTrue(deadlineYes.isCompleted());

        Deadline deadlineNo = Deadline.createTaskFromDataParts(
                new String[]{"D", "Submit report", "N", "28/02/2026 1430"});
        assertFalse(deadlineNo.isCompleted());
    }

    /**
     * Tests that createTaskFromDataParts throws exception for invalid formats of inputs.
     */
    @Test
    public void createTaskFromDataParts_invalidFormats_throwsStorageException() {
        assertThrows(StorageException.class, () ->
                Deadline.createTaskFromDataParts(new String[]{"D", "Submit report", "N"}));

        assertThrows(StorageException.class, () -> Deadline.createTaskFromDataParts(
                        new String[]{"D", "Submit report", "N", "wrong 213 date format wasd"}));

        assertThrows(StorageException.class, () ->
                Deadline.createTaskFromDataParts(new String[]{"D", "   ", "N", "28/02/2026 1430"}));

        assertThrows(StorageException.class, () ->
                Deadline.createTaskFromDataParts(new String[]{"D", "Submit report", "X", "28/02/2026 1430"}));

        assertThrows(StorageException.class, () ->
                Deadline.createTaskFromDataParts(new String[]{"X", "Submit report", "N", "28/02/2026 1430"}));
    }

}
