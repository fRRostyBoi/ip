package notjippity.commands;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.exceptions.NjException;
import notjippity.notes.Note;
import notjippity.notes.NoteTracker;

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

        String firstArg = argStr.trim().split(" ")[0];
        String remainderArgs = argStr.replaceFirst(firstArg, "").trim();

        System.out.println(firstArg + " " + remainderArgs);

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

        return convertToFoundList(noteTracker.getNotes());
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
     * @param remainingArgs The note's index as listed in the "notes" command
     * @return The response message after deleting the note
     * @throws MissingArgException If the input doesn't exist.
     * @throws InvalidArgException If the input is not an integer, or there is no note with that index.
     * @throws CmdFormatException  If the
     */
    private List<String> executeRemove(String remainingArgs)
            throws MissingArgException, InvalidArgException, CmdFormatException {
        if (remainingArgs.isBlank()) {
            throw new MissingArgException("You gotta give me a number, man. (" + FORMAT_CMD + ")");
        }

        Note note = checkAndReturnValidTask(remainingArgs);
        noteTracker.removeNote(note);

        return List.of("-- #" + remainingArgs + " " + note.toString());
    }

    /**
     * Converts the list of notes into a list of response messages.
     *
     * @param notes The list of notes.
     * @return A list of strings representing the notes.
     */
    private List<String> convertToFoundList(List<Note> notes) {
        List<String> response = new ArrayList<>();
        response.add("Here's what you noted down so far:");

        // For indices with lesser digits, add buffer spaces to match highest number of digits.
        // Ensures all strings that come after the indices are flush.
        int lastAddedIndex = 1;
        int maxDigits = 1 + (int) Math.floor(Math.log10(lastAddedIndex));
        for (Note note : notes) {
            int curDigits = 1 + (int) Math.floor(Math.log10(lastAddedIndex));

            StringBuilder indexStr = new StringBuilder(lastAddedIndex++ + ". ");
            for (int i = 0; i < maxDigits - curDigits; i++) {
                indexStr.append(" ");
            }

            response.add(indexStr.toString() + note);
        }

        return response;
    }

    /**
     * Returns the valid note, handling the logic for error cases.
     *
     * @param argStr The argument string.
     * @return The Note object.
     * @throws CmdFormatException  If argStr does not contain a valid integer
     * @throws InvalidArgException If argStr does not contain a valid index
     */
    private Note checkAndReturnValidTask(String argStr) throws CmdFormatException, InvalidArgException {
        try {
            int index = Integer.parseInt(argStr);
            return noteTracker.getNote(index - 1);
        } catch (NumberFormatException exception) {
            throw new CmdFormatException("Idk waddat, enter the index of the note as seen "
                    + "in the \"note\" command instead");
        } catch (IndexOutOfBoundsException exception) {
            throw new InvalidArgException("Uhhhh we don't have note #" + argStr
                    + ", maybe check with \"note\" again?");
        }
    }

}
