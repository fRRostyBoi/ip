package notjippity.notes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import notjippity.exceptions.StorageException;

/**
 * Contains JUnit tests for the Note class.
 */
public class NoteTest {

    /**
     * Tests that getDataString returns the correct storage format.
     */
    @Test
    public void getDataString_validNote_correctFormat() {
        LocalDate date = LocalDate.of(2026, 3, 15);
        Note note = new Note(date, "Important meeting");
        assertEquals("15/03/2026||Important meeting", note.getDataString());
    }

    /**
     * Tests that toString returns the correct display format.
     */
    @Test
    public void toString_validNote_correctFormat() {
        LocalDate date = LocalDate.of(2026, 4, 20);
        Note note = new Note(date, "Call Ben Dover");
        assertEquals("Call Ben Dover [20/04/2026]", note.toString());
    }

    /**
     * Tests that createNoteFromString correctly parses a valid data string.
     */
    @Test
    public void createNoteFromString_validInput_correctlyParsed() throws StorageException {
        Note note = Note.createNoteFromString("18/02/2026||Remember to buy milk");
        assertEquals("Remember to buy milk [18/02/2026]", note.toString());
    }

    /**
     * Tests that createNoteFromString throws exception for various invalid inputs.
     */
    @Test
    public void createNoteFromString_invalidInput_throwsStorageException() {
        // Missing separator
        assertThrows(StorageException.class, () -> Note.createNoteFromString("Invalid note format"));
        // Blank date
        assertThrows(StorageException.class, () -> Note.createNoteFromString("   ||Content here"));
        // Blank content
        assertThrows(StorageException.class, () -> Note.createNoteFromString("18/02/2026||   "));
        // Invalid date format
        assertThrows(StorageException.class, () -> Note.createNoteFromString("invalid-date||Content"));
        // Too many parts
        assertThrows(StorageException.class, () -> Note.createNoteFromString("18/02/2026||Content||Extra stuff||Here"));
    }

}
