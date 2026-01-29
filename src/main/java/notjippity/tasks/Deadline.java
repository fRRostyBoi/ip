package notjippity.tasks;

import notjippity.exceptions.StorageException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {

    public static final String DATE_FORMAT = "dd/MM/yyyy HHmm";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private LocalDateTime byDateTime;

    public Deadline(String name, LocalDateTime byDateTime) {
        super(name);
        this.byDateTime = byDateTime;
    }

    private Deadline(String name, boolean completed, LocalDateTime byDateTime) {
        super(name, completed);
        this.byDateTime = byDateTime;
    }

    /**
     * Checks if the deadline ByDate matches the given date
     * @return True if the deadline matches
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
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (completed ? "Y" : "N") + DATA_SEPARATOR
                + byDateTime.format(DATETIME_FORMATTER);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + byDateTime.format(DATETIME_FORMATTER) + "]";
    }

    /**
     * Converts data part strings into a Deadline task
     * @param dataParts The data parts from Task.createTaskFromString
     * @return The Deadline Task
     * @throws StorageException If any data part is of an invalid format
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

        boolean status = false;
        if (statusStr.equals("Y")) {
            status = true;
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

        return new Deadline(name, status, byDate);
    }

}
