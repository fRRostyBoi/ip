package notjippity.io;

import java.util.ArrayList;
import java.util.List;

import notjippity.exceptions.StorageException;
import notjippity.notes.Note;

/**
 * Represents the bot's persistent data storage system for notes.
 */
public class NoteStorage extends Storage {

    private static final String REL_FILE_PATH = "data/notes.txt";

    /**
     * Returns a new NoteStorage instance.
     */
    public NoteStorage() {
        super(REL_FILE_PATH);
    }

    /**
     * Attempts to parse the note data contained in the file into a collection of Notes.
     *
     * @return The list of notes loaded from file.
     * @throws StorageException If the file content is of the wrong format/corrupted.
     */
    public List<Note> loadNotes() throws StorageException {
        ArrayList<Note> notes = new ArrayList<>();

        int index = 1;
        try {
            for (String dataString : loadData()) {
                notes.add(Note.createNoteFromString(dataString));
                index++;
            }
        } catch (StorageException exception) {
            throw new StorageException("Invalid file format on line " + index + ": " + exception.getMessage());
        }

        return notes;
    }

    /**
     * Saves all Notes to file with the provided List of data strings.
     *
     * @param dataStrings The list of data strings given by TaskTracker.getAllDataStrings().
     * @throws StorageException If an I/O error occurs during the saving process.
     */
    public void saveNotes(List<String> dataStrings) throws StorageException {
        saveData(dataStrings);
    }

}
