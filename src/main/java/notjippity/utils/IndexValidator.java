package notjippity.utils;

import notjippity.exceptions.CmdFormatException;
import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.MissingArgException;
import notjippity.notes.Note;
import notjippity.notes.NoteTracker;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;

/**
 * Utility class for validating and retrieving items by index.
 */
public class IndexValidator {

    private static final String FORMAT_CMD_TEMPLATE = "Format: %s <Id>";

    /**
     * Validates that the argument string is not null.
     *
     * @param argStr  The argument string.
     * @param cmdName The command name for error messages.
     * @throws MissingArgException If the arg string is null.
     */
    public static void validateNotMissing(String argStr, String cmdName) throws MissingArgException {
        if (argStr == null) {
            throw new MissingArgException("Which one? (" + String.format(FORMAT_CMD_TEMPLATE, cmdName) + ")");
        }
    }

    /**
     * Returns the valid task, handling the logic for error cases.
     *
     * @param argStr      The argument string.
     * @param taskTracker The task tracker to retrieve from.
     * @return The Task object.
     * @throws CmdFormatException  If argStr does not contain a valid integer.
     * @throws InvalidArgException If argStr does not contain a valid index.
     */
    public static Task getValidTask(String argStr, TaskTracker taskTracker)
            throws CmdFormatException, InvalidArgException {
        int index = parseIndex(argStr, "task", "list");
        return getTaskAtIndex(index, argStr, taskTracker);
    }

    /**
     * Returns the valid note, handling the logic for error cases.
     *
     * @param argStr      The argument string.
     * @param noteTracker The note tracker to retrieve from.
     * @return The Note object.
     * @throws CmdFormatException  If argStr does not contain a valid integer.
     * @throws InvalidArgException If argStr does not contain a valid index.
     */
    public static Note getValidNote(String argStr, NoteTracker noteTracker)
            throws CmdFormatException, InvalidArgException {
        int index = parseIndex(argStr, "note", "notes");
        return getNoteAtIndex(index, argStr, noteTracker);
    }

    private static int parseIndex(String argStr, String itemType, String listCommand) throws CmdFormatException {
        try {
            return Integer.parseInt(argStr);
        } catch (NumberFormatException exception) {
            throw new CmdFormatException("Idk waddat, enter the index of the " + itemType + " as seen "
                    + "in the \"" + listCommand + "\" command instead");
        }
    }

    private static Task getTaskAtIndex(int index, String argStr, TaskTracker taskTracker)
            throws InvalidArgException {
        try {
            return taskTracker.getTask(index - 1);
        } catch (IndexOutOfBoundsException exception) {
            throw new InvalidArgException("Uhhhh we don't have task #" + argStr
                    + ", maybe check with \"list\" again?");
        }
    }

    private static Note getNoteAtIndex(int index, String argStr, NoteTracker noteTracker)
            throws InvalidArgException {
        try {
            return noteTracker.getNote(index - 1);
        } catch (IndexOutOfBoundsException exception) {
            throw new InvalidArgException("Uhhhh we don't have note #" + argStr
                    + ", maybe check with \"notes\" again?");
        }
    }
}

