package notjippity.notes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Contains JUnit tests for the NoteTracker class.
 */
public class NoteTrackerTest {

    private NoteTracker noteTracker;

    /**
     * Sets up a new NoteTracker instance before each test.
     */
    @BeforeEach
    public void setUp() {
        noteTracker = new NoteTracker();
    }

    /**
     * Tests that a newly created NoteTracker has size 0.
     */
    @Test
    public void getSize_newNoteTracker_returnsZero() {
        assertEquals(0, noteTracker.getSize());
    }

    /**
     * Tests that addNote correctly adds a note and increases size.
     */
    @Test
    public void addNote_validNote_correctSize() {
        Note note = new Note(LocalDate.now(), "Test note");

        noteTracker.addNote(note);
        assertEquals(1, noteTracker.getSize());
        noteTracker.addNote(note);
        assertEquals(2, noteTracker.getSize());
        noteTracker.addNote(note);
        noteTracker.addNote(note);
        noteTracker.addNote(note);
        assertEquals(5, noteTracker.getSize());

        noteTracker.removeNote(note);
        assertEquals(4, noteTracker.getSize());
        noteTracker.removeNote(note);
        noteTracker.removeNote(note);
        noteTracker.removeNote(note);
        assertEquals(1, noteTracker.getSize());
    }

    /**
     * Tests that getNote returns the correct note at the specified index.
     */
    @Test
    public void getNote_validIndex_returnsCorrectNote() {
        Note note1 = new Note(LocalDate.now(), "Note 1");
        Note note2 = new Note(LocalDate.now(), "Note 2");
        Note note3 = new Note(LocalDate.now(), "Note 3");
        Note note4 = new Note(LocalDate.now(), "Note 4");
        noteTracker.addNote(note1);
        noteTracker.addNote(note2);
        noteTracker.addNote(note3);
        noteTracker.addNote(note4);

        assertEquals(note1, noteTracker.getNote(0));
        assertEquals(note3, noteTracker.getNote(2));
    }

    /**
     * Tests that getNote throws IndexOutOfBoundsException for invalid indices.
     */
    @Test
    public void getNote_invalidIndices_throwsIndexOutOfBoundsException() {
        noteTracker.addNote(new Note(LocalDate.now(), "Note 1"));
        assertThrows(IndexOutOfBoundsException.class, () -> noteTracker.getNote(5));
        assertThrows(IndexOutOfBoundsException.class, () -> noteTracker.getNote(0));
        assertThrows(IndexOutOfBoundsException.class, () -> noteTracker.getNote(-1));
    }

    /**
     * Tests that getNotes returns a cloned list of notes.
     */
    @Test
    public void getNotes_withNotes_returnsClonedList() {
        Note note1 = new Note(LocalDate.now(), "Note 1");
        Note note2 = new Note(LocalDate.now(), "Note 2");
        noteTracker.addNote(note1);
        noteTracker.addNote(note2);

        List<Note> cloned = noteTracker.getNotes();
        assertEquals(2, cloned.size());
        assertEquals(note1, cloned.get(0));
        assertEquals(note2, cloned.get(1));

        // Verify it's a clone by modifying the returned list. Original should remain unchanged.
        cloned.clear();
        assertEquals(2, noteTracker.getSize());
    }

    /**
     * Tests that getAllDataStrings returns correct data strings for no notes and all notes.
     */
    @Test
    public void getAllDataStrings_withNotes_returnsCorrectDataStrings() {
        assertTrue(noteTracker.getAllDataStrings().isEmpty());

        LocalDate date = LocalDate.of(2026, 2, 18);
        noteTracker.addNote(new Note(date, "Note 1"));
        noteTracker.addNote(new Note(date, "Note 2"));

        List<String> dataStrings = noteTracker.getAllDataStrings();
        assertEquals(2, dataStrings.size());
        assertEquals("18/02/2026||Note 1", dataStrings.get(0));
        assertEquals("18/02/2026||Note 2", dataStrings.get(1));
    }

}
