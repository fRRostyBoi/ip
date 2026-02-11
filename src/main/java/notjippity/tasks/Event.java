package notjippity.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import notjippity.exceptions.InvalidArgException;
import notjippity.exceptions.StorageException;
import notjippity.utils.Parser;

/**
 * Represents an Event task.
 */
public class Event extends Task {

    public static final String FORMAT_DATE = "dd/MM/yyyy HHmm";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    /**
     * Returns a new Event instance
     *
     * @param name         The task name.
     * @param fromDateTime The DateTime from which this event starts.
     * @param toDateTime   The DateTime from which this event ends.
     */
    public Event(String name, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        super(name);
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;

        assert this.fromDateTime != null;
        assert this.toDateTime != null;
    }

    /**
     * Returns a new Event instance.
     *
     * @param name         The task name.
     * @param isCompleted  Whether the task has been completed.
     * @param fromDateTime The DateTime from which this event starts.
     * @param toDateTime   The DateTime from which this event ends.
     */
    private Event(String name, boolean isCompleted, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        this(name, fromDateTime, toDateTime);
        this.isCompleted = isCompleted;
    }

    /**
     * Checks if the given date falls between the event date range (both inclusive).
     *
     * @return True if any date in the date range matches.
     */
    public boolean hasDate(LocalDate date) {
        assert date != null;

        ChronoLocalDate fromDate = ChronoLocalDate.from(fromDateTime);
        ChronoLocalDate toDate = ChronoLocalDate.from(toDateTime);

        boolean isEqualFromOrTo = date.isEqual(fromDate) || date.isEqual(toDate);
        boolean isBetweenFromAndTo = (date.isAfter(fromDate) && date.isBefore(toDate));

        return isEqualFromOrTo || isBetweenFromAndTo;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String getDataString() {
        // Format: D||Task_Name||Y/N||FromDate||ToDate
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (isCompleted ? "Y" : "N") + DATA_SEPARATOR
                + fromDateTime.format(DATETIME_FORMATTER) + DATA_SEPARATOR
                + toDateTime.format(DATETIME_FORMATTER);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + fromDateTime.format(DATETIME_FORMATTER)
                + " - " + toDateTime.format(DATETIME_FORMATTER) + "]";
    }

    /**
     * Constructs an Event instance from data part strings.
     *
     * @param dataParts The data parts from Task.createTaskFromString.
     * @return The Event instance.
     * @throws StorageException If any data part has an invalid format.
     */
    public static Event createTaskFromDataParts(String[] dataParts) throws StorageException {
        assert dataParts != null;

        checkDataParts(dataParts);

        String name = getNamePart(dataParts[1]);
        boolean isCompleted = getStatusPart(dataParts[2]);
        LocalDateTime fromDate = getDateTimePart(dataParts[3], 4);
        LocalDateTime toDate = getDateTimePart(dataParts[4], 5);

        return new Event(name, isCompleted, fromDate, toDate);
    }

    /**
     * Throws an error if dataParts length is invalid.
     *
     * @param dataParts The dataParts object.
     * @throws StorageException If dataParts length != 5.
     */
    private static void checkDataParts(String[] dataParts) throws StorageException {
        if (dataParts.length < 5) {
            throw new StorageException("Insufficient arguments; expected 5 but found" + dataParts.length);
        }
    }

    /**
     * Returns the name string
     *
     * @param name The name string.
     * @throws StorageException If name string is blank.
     */
    private static String getNamePart(String name) throws StorageException {
        if (name.isBlank()) {
            throw new StorageException("Invalid argument #1; expected Task name but found empty string");
        }
        return name;
    }

    /**
     * Parses the status string into a boolean.
     *
     * @param statusStr The status string.
     * @throws StorageException If the status string does not match a boolean.
     */
    private static boolean getStatusPart(String statusStr) throws StorageException {
        boolean isCompleted = false;
        if (statusStr.equals("Y")) {
            isCompleted = true;
        } else if (!statusStr.equals("N")) {
            throw new StorageException("Invalid argument #3; expected Y/N but found " + statusStr);
        }
        return isCompleted;
    }

    /**
     * Parses the date string into a LocalDateTime object.
     *
     * @param dateStr The date string.
     * @param argIndex  The date string's argument index.
     * @throws StorageException If the byDate string does not match the format or is blank.
     */
    private static LocalDateTime getDateTimePart(String dateStr, int argIndex) throws StorageException {
        LocalDateTime dateTime;

        try {
            dateTime = Parser.parseDateTime(dateStr, DATETIME_FORMATTER);
        } catch (InvalidArgException exception) {
            throw new StorageException("Invalid argument #" + argIndex + "; expected ByDate but found empty string");
        } catch (DateTimeParseException exception) {
            throw new StorageException("Invalid argument #4" + argIndex + "; expected format " + FORMAT_DATE
                    + " but found " + dateStr);
        }

        return dateTime;
    }

}
