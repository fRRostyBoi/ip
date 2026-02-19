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
 * Contains JUnit tests for the Event class.
 */
public class EventTest {

    /**
     * Tests that getTypeIcon returns the correct type icon for Event tasks.
     */
    @Test
    public void getTypeIcon_normalEvent_returnsEvent() {
        LocalDateTime fromDate = LocalDateTime.of(2026, 3, 1, 9, 0);
        LocalDateTime toDate = LocalDateTime.of(2026, 3, 1, 17, 0);
        Event event = new Event("Test event", fromDate, toDate);
        assertEquals("E", event.getTypeIcon());
    }

    /**
     * Tests that getDataString returns the correct storage format for both incomplete and completed.
     */
    @Test
    public void getDataString_bothStatuses_correctFormat() {
        LocalDateTime fromDate = LocalDateTime.of(2026, 4, 10, 10, 0);
        LocalDateTime toDate = LocalDateTime.of(2026, 4, 10, 12, 0);
        Event event = new Event("Test event", fromDate, toDate);
        assertEquals("E||Test event||N||10/04/2026 1000||10/04/2026 1200", event.getDataString());

        event.complete();
        assertEquals("E||Test event||Y||10/04/2026 1000||10/04/2026 1200", event.getDataString());
    }

    /**
     * Tests that hasDate returns correct boolean for various date scenarios.
     */
    @Test
    public void hasDate_variousDates_returnsCorrectBoolean() {
        LocalDateTime fromDate = LocalDateTime.of(2026, 6, 15, 9, 0);
        LocalDateTime toDate = LocalDateTime.of(2026, 6, 17, 17, 0);
        Event event = new Event("Test event", fromDate, toDate);

        // Start date
        assertTrue(event.hasDate(LocalDate.of(2026, 6, 15)));
        // End date
        assertTrue(event.hasDate(LocalDate.of(2026, 6, 17)));
        // Within range
        assertTrue(event.hasDate(LocalDate.of(2026, 6, 16)));
        // Before range
        assertFalse(event.hasDate(LocalDate.of(2026, 6, 14)));
        // After range
        assertFalse(event.hasDate(LocalDate.of(2026, 6, 18)));
    }

    /**
     * Tests that createTaskFromDataParts correctly parses valid data with different completion statuses.
     */
    @Test
    public void createTaskFromDataParts_validData_correctlyParsed() throws StorageException {
        String[] incompleteData = {"E", "Team meeting", "N", "01/03/2026 0900", "01/03/2026 1700"};
        Event incompleteEvent = Event.createTaskFromDataParts(incompleteData);
        assertFalse(incompleteEvent.isCompleted());
        assertTrue(incompleteEvent.hasDate(LocalDate.of(2026, 3, 1)));

        String[] completedData = {"E", "Team meeting", "Y", "01/03/2026 0900", "01/03/2026 1700"};
        Event completedEvent = Event.createTaskFromDataParts(completedData);
        assertTrue(completedEvent.isCompleted());
    }

    /**
     * Tests that createTaskFromDataParts throws exception for various invalid inputs.
     */
    @Test
    public void createTaskFromDataParts_invalidInput_throwsStorageException() {
        // Insufficient parts
        String[] insufficientParts = {"E", "Team meeting", "N", "01/03/2026 0900"};
        assertThrows(StorageException.class, () -> Event.createTaskFromDataParts(insufficientParts));

        // Invalid date format
        String[] invalidDate = {"E", "Team meeting", "N", "wrong date-format 12938123", "01/03/2026 1700"};
        assertThrows(StorageException.class, () -> Event.createTaskFromDataParts(invalidDate));

        // Blank name
        String[] blankName = {"E", "   ", "N", "01/03/2026 0900", "01/03/2026 1700"};
        assertThrows(StorageException.class, () -> Event.createTaskFromDataParts(blankName));

        // Invalid completion status
        String[] invalidStatus = {"E", "Team meeting", "X", "01/03/2026 0900", "01/03/2026 1700"};
        assertThrows(StorageException.class, () -> Event.createTaskFromDataParts(invalidStatus));
    }

    /**
     * Tests that toString returns the correct format with date range.
     */
    @Test
    public void toString_validEvent_includesFormattedDateRange() {
        LocalDateTime fromDate = LocalDateTime.of(2026, 8, 5, 10, 0);
        LocalDateTime toDate = LocalDateTime.of(2026, 8, 5, 14, 0);
        Event event = new Event("Workshop", fromDate, toDate);

        assertEquals("[  ][E] Workshop [05/08/2026 1000 - 05/08/2026 1400]", event.toString());
    }

}
