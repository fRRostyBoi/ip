package notjippity.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import notjippity.exceptions.StorageException;

/**
 * Represents an Event task
 */
public class Event extends Task {

    public static final String FORMAT_DATE = "dd/MM/yyyy HHmm";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

    private LocalDateTime fromDateTime, toDateTime;

    /**
     * Returns a new Event instance
     *
     * @param name The task name
     * @param fromDateTime The DateTime from which this event starts
     * @param toDateTime The DateTime from which this event ends
     */
    public Event(String name, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        super(name);
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    /**
     * Returns a new Event instance
     *
     * @param name The task name
     * @param completed Whether the task has been completed
     * @param fromDateTime The DateTime from which this event starts
     * @param toDateTime The DateTime from which this event ends
     */
    private Event(String name, boolean completed, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        super(name, completed);
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    /**
     * Checks if the given date falls between the event date range (both inclusive)
     *
     * @return True if any date in the date range matches
     */
    public boolean hasDate(LocalDate date) {
        ChronoLocalDate fromDate = ChronoLocalDate.from(fromDateTime),
                toDate = ChronoLocalDate.from(toDateTime);
        return date.isEqual(fromDate) || date.isEqual(toDate) || (date.isAfter(fromDate) && date.isBefore(toDate));
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String getDataString() {
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
     * Constructs an Event instance from data part strings
     *
     * @param dataParts The data parts from Task.createTaskFromString
     * @return The Event instance
     * @throws StorageException If any data part has an invalid format
     */
    public static Event createTaskFromDataParts(String[] dataParts) throws StorageException {
        if (dataParts.length < 5) {
            throw new StorageException("Insufficient arguments; expected 5 but found" + dataParts.length);
        }

        String name = dataParts[1],
                statusStr = dataParts[2];
        if (name.isEmpty()) {
            throw new StorageException("Invalid argument #1; expected Task name but found empty string");
        }

        boolean isCompleted = false;
        if (statusStr.equals("Y")) {
            isCompleted = true;
        } else if (!statusStr.equals("N")) {
            throw new StorageException("Invalid argument #3; expected Y/N but found " + statusStr);
        }

        String fromStr = dataParts[3],
                toStr = dataParts[4];
        LocalDateTime fromDate, toDate;

        if (fromStr.isEmpty()) {
            throw new StorageException("Invalid argument #4; expected FromDate but found empty string");
        }
        if (toStr.isEmpty()) {
            throw new StorageException("Invalid argument #5; expected ToDate but found empty string");
        }
        try {
            fromDate = LocalDateTime.parse(fromStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new StorageException("Invalid argument #4; expected format " + FORMAT_DATE
                    + " but found " + fromStr);
        }
        try {
            toDate = LocalDateTime.parse(toStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new StorageException("Invalid argument #5; expected format " + FORMAT_DATE
                    + " but found " + fromStr);
        }

        return new Event(name, isCompleted, fromDate, toDate);
    }

}
