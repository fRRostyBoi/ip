package tasks;

import exceptions.StorageException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {

    public static final String DATE_FORMAT = "dd/MM/yyyy HHmm";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private LocalDateTime fromDateTime, toDateTime;

    public Event(String name, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        super(name);
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    private Event(String name, boolean completed, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        super(name, completed);
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    /**
     * Checks if any date in the event date range matches the given date
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
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (completed ? "Y" : "N") + DATA_SEPARATOR
                + fromDateTime.format(DATETIME_FORMATTER) + DATA_SEPARATOR
                + toDateTime.format(DATETIME_FORMATTER);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + fromDateTime.format(DATETIME_FORMATTER)
                + " - " + toDateTime.format(DATETIME_FORMATTER) + "]";
    }

    /**
     * Converts data part strings into a Event task
     * @param dataParts The data parts from Task.createTaskFromString
     * @return The Event Task
     * @throws StorageException If any data part is of an invalid format
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

        boolean status = false;
        if (statusStr.equals("Y")) {
            status = true;
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
            throw new StorageException("Invalid argument #4; expected format " + DATE_FORMAT
                    + " but found " + fromStr);
        }
        try {
            toDate = LocalDateTime.parse(toStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new StorageException("Invalid argument #5; expected format " + DATE_FORMAT
                    + " but found " + fromStr);
        }

        return new Event(name, status, fromDate, toDate);
    }

}
