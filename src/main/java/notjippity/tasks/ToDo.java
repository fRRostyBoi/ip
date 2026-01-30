package notjippity.tasks;

import notjippity.exceptions.StorageException;

/**
 * Represents a ToDo task
 */
public class ToDo extends Task {

    /**
     * Returns a new ToDo instance
     *
     * @param name The task name
     */
    public ToDo(String name) {
        super(name);
    }

    /**
     * Returns a new ToDo instance
     *
     * @param name The task name
     * @param completed Whether the task has been completed
     */
    private ToDo(String name, boolean completed) {
        super(name, completed);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }

    @Override
    public String getDataString() {
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (isCompleted ? "Y" : "N");
    }

    /**
     * Constructs a ToDo instance from data part strings
     *
     * @param dataParts The data parts from Task.createTaskFromString
     * @return The ToDo instance
     * @throws StorageException If any data part has an invalid format
     */
    public static ToDo createTaskFromDataParts(String[] dataParts) throws StorageException {
        if (dataParts.length < 3) {
            throw new StorageException("Insufficient arguments; expected 3 but found" + dataParts.length);
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

        return new ToDo(name, isCompleted);
    }

}
