package notjippity.commands;

import java.time.LocalDate;
import java.util.List;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.exceptions.NjException;
import notjippity.notes.Note;
import notjippity.notes.NoteTracker;
import notjippity.utils.IndexValidator;
import notjippity.utils.ListFormatter;

/**
 * Handles "notes" command logic and behaviour.
 */
public class NoteCmd extends Command {

    private static final String FORMAT_CMD = "Format: notes [add/delete] [...]";

    private final NoteTracker noteTracker;

    /**
     * Returns a new NoteCmd instance.
     *
     * @param noteTracker The bot's note tracker.
     */
    public NoteCmd(NoteTracker noteTracker) {
        super("notes");
        this.noteTracker = noteTracker;
    }

    /**
     * Lists all notes if no arguments are provided.
     * If command begins with "notes add", adds the note with the details in argStr
     * If command begins with "notes remove", removes the note with details in argStr
     *
     * @param cmdStr The command string.
     * @param argStr The string of arguments.
     * @return The bot's response.
     * @throws CmdFormatException  If the command is of an invalid format.
     * @throws MissingArgException If there are missing arguments.
     * @throws InvalidArgException If there are invalid arguments.
     */
    @Override
    public List<String> execute(String cmdStr, String argStr) throws NjException {
        if (argStr == null || argStr.isBlank()) {
            return executeList();
        }

        String[] parts = argStr.trim().split(" ", 2);
        String firstArg = parts[0];
        String remainderArgs = parts.length > 1 ? parts[1].trim() : "";

        return switch (firstArg.toLowerCase()) {
        case "add" -> executeAdd(remainderArgs);
        case "delete" -> executeRemove(remainderArgs);
        default -> throw new InvalidArgException("Uhhhh, what's " + firstArg + "? (" + FORMAT_CMD + ")");
        };
    }

    /**
     * Returns the bot's response for listing currently stored notes
     *
     * @return The bot's response
     */
    private List<String> executeList() {
        if (noteTracker.getSize() == 0) {
            return List.of("No notes found, wanna add some?");
        }

        return ListFormatter.formatNoteList(noteTracker.getNotes(), "Here's what you noted down so far:");
    }

    /**
     * Adds a note with the specified content. The date of the note will be the
     * device's local date when the command was run.
     *
     * @param remainingArgs The content of the note.
     * @return The response after creating the note.
     * @throws MissingArgException If the content provided is empty.
     */
    private List<String> executeAdd(String remainingArgs) throws MissingArgException {
        if (remainingArgs.isBlank()) {
            throw new MissingArgException("Uh yeah, what's the note you want me to remember? (" + FORMAT_CMD + ")");
        }

        Note note = new Note(LocalDate.now(), remainingArgs);
        noteTracker.addNote(note);

        return List.of("++ " + note + " (" + noteTracker.getSize() + " total)");
    }

    /**
     * Removes a note with the specified index.
     *
     * @param remainingArgs The note's index as listed in the "notes" command.
     * @return The response message after deleting the note.
     * @throws MissingArgException If the input doesn't exist.
     * @throws InvalidArgException If the input is not an integer, or there is no note with that index.
     * @throws CmdFormatException  If the command format is invalid.
     */
    private List<String> executeRemove(String remainingArgs) throws
            MissingArgException, InvalidArgException, CmdFormatException {
        if (remainingArgs.isBlank()) {
            throw new MissingArgException("You gotta give me a number, man. (" + FORMAT_CMD + ")");
        }

        Note note = IndexValidator.getValidNote(remainingArgs, noteTracker);
        noteTracker.removeNote(note);

        return List.of("-- #" + remainingArgs + " " + note.toString());
    }
}
