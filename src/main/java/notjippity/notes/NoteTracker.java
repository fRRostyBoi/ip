package notjippity.notes;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks all notes and provides functions to maintain notes.
 */
public class NoteTracker {

    private ArrayList<Note> notes = new ArrayList<>();

    /**
     * Adds a Note into the list.
     *
     * @param note The note to be added.
     */
    public void addNote(Note note) {
        assert note != null;
        notes.add(note);
    }

    /**
     * Removes a Note from the list.
     *
     * @param note The note to remove.
     */
    public void removeNote(Note note) {
        assert note != null;
        notes.remove(note);
    }

    /**
     * Returns the list of notes.
     *
     * @return The list of notes.
     */
    public List<Note> getNotes() {
        return (List<Note>) notes.clone();
    }

    /**
     * Returns the size of the note list.
     *
     * @return The size of the note list.
     */
    public int getSize() {
        return notes.size();
    }

    /**
     * Converts all Notes into data string form and returns it as a String list.
     * in the same order as the original Note list.
     *
     * @return The list of data strings.
     */
    public List<String> getAllDataStrings() {
        return notes.stream().map(Note::getDataString).toList();
    }

    /**
     * Returns the note specified by the given index, 0-indexed.
     *
     * @param index The index of the note.
     * @return The note specified by the given index.
     * @throws IndexOutOfBoundsException If the provided index is outside the range of the list.
     */
    public Note getNote(int index) throws IndexOutOfBoundsException {
        return notes.get(index);
    }

}
