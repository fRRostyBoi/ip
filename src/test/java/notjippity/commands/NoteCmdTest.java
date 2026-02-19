package notjippity.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.exceptions.NjException;
import notjippity.notes.Note;
import notjippity.notes.NoteTracker;

/**
 * Contains JUnit tests for the NoteCmd class.
 */
public class NoteCmdTest {

    private NoteTracker noteTracker;
    private NoteCmd noteCmd;

    /**
     * Sets up a fresh NoteTracker and NoteCmd before each test.
     */
    @BeforeEach
    public void setUp() {
        noteTracker = new NoteTracker();
        noteCmd = new NoteCmd(noteTracker);
    }

    /**
     * Tests that execute with no arguments returns appropriate response based on note list state.
     */
    @Test
    public void execute_listNotes_returnsCorrectResponse() throws NjException {
        // Empty list
        CmdOutput emptyOutput = noteCmd.execute("notes", null);
        assertFalse(emptyOutput.isError());
        assertTrue(emptyOutput.getReply().get(0).contains("No notes found"));

        // With notes
        noteTracker.addNote(new Note(LocalDate.now(), "Note 1"));
        noteTracker.addNote(new Note(LocalDate.now(), "Note 2"));
        CmdOutput withNotesOutput = noteCmd.execute("notes", null);
        assertFalse(withNotesOutput.isError());
        assertTrue(withNotesOutput.getReply().size() > 1);
        assertTrue(withNotesOutput.getReply().get(0).contains("Here's what you noted"));

        // Blank argument also returns all notes
        CmdOutput blankArgOutput = noteCmd.execute("notes", "   ");
        assertFalse(blankArgOutput.isError());
        assertTrue(blankArgOutput.getReply().get(0).contains("Here's what you noted"));
    }

    /**
     * Tests that execute with "add" adds a new note.
     */
    @Test
    public void execute_addNote_addsNote() throws NjException {
        CmdOutput output = noteCmd.execute("notes", "add Remember to call John");

        assertFalse(output.isError());
        assertEquals(1, noteTracker.getSize());
        assertTrue(output.getReply().get(0).contains("Remember to call John"));
    }

    /**
     * Tests that execute with "add" but no content throws MissingArgException.
     */
    @Test
    public void execute_addWithNoContent_throwsMissingArgException() {
        MissingArgException exception = assertThrows(MissingArgException.class, () ->
                noteCmd.execute("notes", "add"));
        assertTrue(exception.getMessage().contains("what's the note"));
    }

    /**
     * Tests that execute with "add" but blank content throws MissingArgException.
     */
    @Test
    public void execute_addWithBlankContent_throwsMissingArgException() {
        MissingArgException exception = assertThrows(MissingArgException.class, () ->
                noteCmd.execute("notes", "add    "));
        assertTrue(exception.getMessage().contains("what's the note"));
    }

    /**
     * Tests that execute with "delete" removes a note.
     */
    @Test
    public void execute_deleteNote_removesNoteFromTracker() throws NjException {
        noteTracker.addNote(new Note(LocalDate.now(), "Note to delete"));
        assertEquals(1, noteTracker.getSize());

        CmdOutput output = noteCmd.execute("notes", "delete 1");

        assertFalse(output.isError());
        assertEquals(0, noteTracker.getSize());
        assertTrue(output.getReply().get(0).contains("Note to delete"));
    }

    /**
     * Tests that execute with "delete" but no index throws MissingArgException.
     */
    @Test
    public void execute_deleteWithInvalidIndices_throwsRespectiveException() {
        noteTracker.addNote(new Note(LocalDate.now(), "Note"));

        MissingArgException missingError = assertThrows(MissingArgException.class, () ->
                noteCmd.execute("notes", "delete"));
        assertTrue(missingError.getMessage().contains("give me a number"));

        CmdFormatException naiError = assertThrows(CmdFormatException.class, () ->
                noteCmd.execute("notes", "delete abc"));
        assertTrue(naiError.getMessage().contains("index"));

        CmdFormatException negError = assertThrows(CmdFormatException.class, () ->
                noteCmd.execute("notes", "delete -1"));
        assertTrue(negError.getMessage().contains("index"));

        InvalidArgException oobError = assertThrows(InvalidArgException.class, () ->
                noteCmd.execute("notes", "delete 99"));
        assertTrue(oobError.getMessage().contains("don't have note"));
    }

    /**
     * Tests that execute with unknown subcommand throws InvalidArgException.
     */
    @Test
    public void execute_unknownSubcommand_throwsInvalidArgException() {
        InvalidArgException exception = assertThrows(InvalidArgException.class, () ->
                noteCmd.execute("notes", "unknown"));
        assertTrue(exception.getMessage().contains("what's unknown"));
    }

    /**
     * Tests that the command name is correctly set.
     */
    @Test
    public void getCmdName_returnsNotes() {
        assertEquals("notes", noteCmd.getCmdName());
    }

}
