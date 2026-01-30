package notjippity.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import notjippity.exceptions.StorageException;

/**
 * Represents a Deadline task
 */
public class Deadline extends Task {

    public static final String DATE_FORMAT = "dd/MM/yyyy HHmm";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private LocalDateTime byDateTime;

    /**
     * Returns a new Deadline instance
     *
     * @param name The task name
     * @param byDateTime The deadline for this task
     */
    public Deadline(String name, LocalDateTime byDateTime) {
        super(name);
        this.byDateTime = byDateTime;
    }

    /**
     * Returns a new Deadline instance
     *
     * @param name The task name
     * @param completed Whether the task has been completed
     * @param byDateTime The deadline for this task
     */
    private Deadline(String name, boolean completed, LocalDateTime byDateTime) {
        super(name, completed);
        this.byDateTime = byDateTime;
    }

    /**
     * Checks if the deadline matches the given date
     *
     * @return True if the deadline matches the given date
     */
    public boolean hasDate(LocalDate date) {
        return date.isEqual(ChronoLocalDate.from(byDateTime));
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String getDataString() {
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (isCompleted ? "Y" : "N") + DATA_SEPARATOR
                + byDateTime.format(DATETIME_FORMATTER);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + byDateTime.format(DATETIME_FORMATTER) + "]";
    }

    /**
     * Constructs a Deadline instance from data part strings
     *
     * @param dataParts The data parts from Task.createTaskFromString
     * @return The Deadline instance
     * @throws StorageException If any data part has an invalid format
     */
    public static Deadline createTaskFromDataParts(String[] dataParts) throws StorageException {
        if (dataParts.length < 4) {
            throw new StorageException("Insufficient arguments; expected 4 but found" + dataParts.length);
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

        String byDateStr = dataParts[3];
        LocalDateTime byDate;
        if (byDateStr.isEmpty()) {
            throw new StorageException("Invalid argument #4; expected ByDate but found empty string");
        }
        try {
            byDate = LocalDateTime.parse(byDateStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new StorageException("Invalid argument #4; expected format " + DATE_FORMAT
                    + " but found " + byDateStr);
        }

        return new Deadline(name, isCompleted, byDate);
    }

}
