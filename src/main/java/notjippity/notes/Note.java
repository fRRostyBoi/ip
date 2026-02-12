package notjippity.notes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.StorageException;
import notjippity.utils.Parser;

/**
 * Represents a Note.
 */
public class Note {

    private static final String DATA_SEPARATOR = "||";
    private static final String DATA_SPLITTER = "\\|\\|";

    private static final String FORMAT_DATE = "dd/MM/yyyy";
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

    private LocalDate dateAdded;

    private final String content;

    /**
     * Returns a new Note instance
     *
     * @param dateAdded The creation date
     * @param content   The note contents
     */
    public Note(LocalDate dateAdded, String content) {
        boolean isValidContent = content != null && !content.isBlank();
        boolean isDateValid = dateAdded != null;
        assert isValidContent && isDateValid;

        this.dateAdded = dateAdded;
        this.content = content;
    }

    /**
     * Converts this Note into its data string format, which may be saved to/loaded from a file.
     *
     * @return The Note data in string format.
     */
    public String getDataString() {
        // Format: Date_Added||Content
        return dateAdded.format(DATETIME_FORMATTER) + DATA_SEPARATOR + content;
    }

    /**
     * Returns the printable string representation of this note.
     *
     * @return The printable string representation.
     */
    @Override
    public String toString() {
        return content + " [" + dateAdded.format(DATETIME_FORMATTER) + "]";
    }

    /**
     * Returns the respective Note object according to its type given the data string.
     * Each Note's data string must match the format returned from its getDataString() method.
     *
     * @param dataStr The data string.
     * @return The note instance.
     * @throws StorageException If the data string contains an invalid type or data format.
     */
    public static Note createNoteFromString(String dataStr) throws StorageException {
        boolean isValidInput = dataStr != null && !dataStr.isBlank();
        assert isValidInput;

        String[] dataParts = extractDataParts(dataStr);
        String dateAddedStr = dataParts[0].trim();
        String content = dataParts[1].trim();

        LocalDate dateAdded = getDateTimePart(dateAddedStr);

        return new Note(dateAdded, content);
    }

    /**
     * Extracts the data parts necessary to form the string from the given data string.
     * Also checks for any blank contents for any data part, throwing an error if so.
     *
     * @param dataStr The data string
     * @return The data part array, in the order specified by Note.getDataString()
     * @throws StorageException If any data part is blank
     */
    private static String[] extractDataParts(String dataStr) throws StorageException {
        String[] dataParts = dataStr.split(DATA_SPLITTER);
        if (dataParts.length != 2) {
            throw new StorageException("Invalid Note data string format");
        }

        String dateAddedStr = dataParts[0];
        String content = dataParts[1];

        assert dateAddedStr != null && content != null;

        boolean hasContent = !dateAddedStr.isBlank() && !content.isBlank();
        if (!hasContent) {
            throw new StorageException("Invalid Note data string parts: Date Added or Content is blank");
        }

        return dataParts;
    }

    /**
     * Parses the date string into a LocalDateTime object.
     *
     * @param dateStr The date string.
     * @throws StorageException If the byDate string does not match the format or is blank.
     */
    private static LocalDate getDateTimePart(String dateStr) throws StorageException {
        LocalDate date;

        try {
            date = Parser.parseDate(dateStr, DATETIME_FORMATTER);
        } catch (InvalidArgException exception) {
            throw new StorageException("Invalid argument #1; expected DateAdded but found empty string");
        } catch (DateTimeParseException exception) {
            throw new StorageException("Invalid argument #1; expected format " + FORMAT_DATE
                    + " but found " + dateStr);
        }

        return date;
    }

}
