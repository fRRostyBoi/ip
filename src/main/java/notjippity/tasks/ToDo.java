package notjippity.tasks;

import notjippity.exceptions.StorageException;

/**
 * Represents a ToDo task.
 */
public class ToDo extends Task {

    /**
     * Returns a new ToDo instance.
     *
     * @param name The task name.
     */
    public ToDo(String name) {
        super(name);
    }

    /**
     * Returns a new ToDo instance
     *
     * @param name The task name.
     * @param isCompleted Whether the task has been completed.
     */
    private ToDo(String name, boolean isCompleted) {
        super(name, isCompleted);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }

    @Override
    public String getDataString() {
        // Format: D||Task_Name||Y/N
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (isCompleted ? "Y" : "N");
    }

    /**
     * Constructs a ToDo instance from data part strings.
     *
     * @param dataParts The data parts from Task.createTaskFromString.
     * @return The ToDo instance.
     * @throws StorageException If any data part has an invalid format.
     */
    public static ToDo createTaskFromDataParts(String[] dataParts) throws StorageException {
        assert dataParts != null;

        checkDataParts(dataParts);

        String name = getNamePart(dataParts[1]);
        boolean isCompleted = getStatusPart(dataParts[2]);

        return new ToDo(name, isCompleted);
    }

    /**
     * Throws an error if dataParts length is invalid.
     *
     * @param dataParts The dataParts object.
     * @throws StorageException If dataParts length != 3.
     */
    private static void checkDataParts(String[] dataParts) throws StorageException {
        if (dataParts.length < 3) {
            throw new StorageException("Insufficient arguments; expected 3 but found" + dataParts.length);
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

}
