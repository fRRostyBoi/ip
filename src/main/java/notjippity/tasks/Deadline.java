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
 * Represents a Deadline task.
 */
public class Deadline extends Task {

    public static final String FORMAT_DATE = "dd/MM/yyyy HHmm";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

    private LocalDateTime byDateTime;

    /**
     * Returns a new Deadline instance.
     *
     * @param name       The task name.
     * @param byDateTime The deadline for this task.
     */
    public Deadline(String name, LocalDateTime byDateTime) {
        super(name);
        this.byDateTime = byDateTime;
    }

    /**
     * Returns a new Deadline instance.
     *
     * @param name        The task name.
     * @param isCompleted Whether the task has been completed.
     * @param byDateTime  The deadline for this task.
     */
    private Deadline(String name, boolean isCompleted, LocalDateTime byDateTime) {
        super(name, isCompleted);
        this.byDateTime = byDateTime;
    }

    /**
     * Checks if the deadline matches the given date.
     *
     * @return True if the deadline matches the given date.
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
        // Format: D||Task_Name||Y/N||ByDate
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (isCompleted ? "Y" : "N") + DATA_SEPARATOR
                + byDateTime.format(DATETIME_FORMATTER);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + byDateTime.format(DATETIME_FORMATTER) + "]";
    }

    /**
     * Constructs a Deadline instance from data part strings.
     *
     * @param dataParts The data parts from Task.createTaskFromString.
     * @return The Deadline instance.
     * @throws StorageException If any data part has an invalid format.
     */
    public static Deadline createTaskFromDataParts(String[] dataParts) throws StorageException {
        checkDataParts(dataParts);

        String name = getNamePart(dataParts[1]);
        boolean isCompleted = getStatusPart(dataParts[2]);
        LocalDateTime byDate = getDateTimePart(dataParts[3]);

        return new Deadline(name, isCompleted, byDate);
    }

    /**
     * Throws an error if dataParts length is invalid.
     *
     * @param dataParts The dataParts object.
     * @throws StorageException If dataParts length != 4.
     */
    private static void checkDataParts(String[] dataParts) throws StorageException {
        if (dataParts.length < 4) {
            throw new StorageException("Insufficient arguments; expected 4 but found" + dataParts.length);
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
     * Parses the byDate string into a LocalDateTime object.
     *
     * @param byDateStr The byDate string.
     * @throws StorageException If the byDate string does not match the format or is blank.
     */
    private static LocalDateTime getDateTimePart(String byDateStr) throws StorageException {
        LocalDateTime byDate;

        try {
            byDate = Parser.parseDateTime(byDateStr, DATETIME_FORMATTER);
        } catch (InvalidArgException exception) {
            throw new StorageException("Invalid argument #4; expected ByDate but found empty string");
        } catch (DateTimeParseException exception) {
            throw new StorageException("Invalid argument #4; expected format " + FORMAT_DATE
                    + " but found " + byDateStr);
        }

        return byDate;
    }

}
