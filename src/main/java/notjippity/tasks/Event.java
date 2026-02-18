package notjippity.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

import notjippity.exceptions.StorageException;
import notjippity.utils.TaskDataParser;

/**
 * Represents an Event task.
 */
public class Event extends Task {

    public static final String FORMAT_DATE = "dd/MM/yyyy HHmm";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    /**
     * Returns a new Event instance.
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

        boolean isOnBoundary = date.isEqual(fromDate) || date.isEqual(toDate);
        if (isOnBoundary) {
            return true;
        }

        boolean isWithinRange = date.isAfter(fromDate) && date.isBefore(toDate);
        return isWithinRange;
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

        TaskDataParser.validateDataPartsLength(dataParts, 5);

        String name = TaskDataParser.parseName(dataParts[1]);
        boolean isCompleted = TaskDataParser.parseCompletionStatus(dataParts[2]);
        LocalDateTime fromDate = TaskDataParser.parseDateTime(dataParts[3], DATETIME_FORMATTER, 4);
        LocalDateTime toDate = TaskDataParser.parseDateTime(dataParts[4], DATETIME_FORMATTER, 5);

        return new Event(name, isCompleted, fromDate, toDate);
    }

}
