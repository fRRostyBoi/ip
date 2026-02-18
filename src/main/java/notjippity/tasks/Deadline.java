package notjippity.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

import notjippity.exceptions.StorageException;
import notjippity.utils.TaskDataParser;

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
        assert this.byDateTime != null;
    }

    /**
     * Returns a new Deadline instance.
     *
     * @param name        The task name.
     * @param isCompleted Whether the task has been completed.
     * @param byDateTime  The deadline for this task.
     */
    private Deadline(String name, boolean isCompleted, LocalDateTime byDateTime) {
        this(name, byDateTime);
        this.isCompleted = isCompleted;
    }

    /**
     * Checks if the deadline matches the given date.
     *
     * @return True if the deadline matches the given date.
     */
    public boolean hasDate(LocalDate date) {
        assert date != null;
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
        assert dataParts != null;

        TaskDataParser.validateDataPartsLength(dataParts, 4);

        String name = TaskDataParser.parseName(dataParts[1]);
        boolean isCompleted = TaskDataParser.parseCompletionStatus(dataParts[2]);
        LocalDateTime byDate = TaskDataParser.parseDateTime(dataParts[3], DATETIME_FORMATTER, 4);

        return new Deadline(name, isCompleted, byDate);
    }

}
