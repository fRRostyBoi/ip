package notjippity.tasks;

import notjippity.exceptions.StorageException;

/**
 * Represents an abstract Task
 */
public abstract class Task {

    protected static final String DATA_SEPARATOR = "||";
    protected static final String DATA_SPLITTER = "\\|\\|";

    protected String name;
    protected boolean isCompleted;

    /**
     * Returns a new Task instance
     *
     * @param name The task name
     */
    protected Task(String name) {
        this.name = name;
    }

    /**
     * Returns a new Task instance
     *
     * @param name The task name
     * @param isCompleted Whether the task has been completed
     */
    protected Task(String name, boolean isCompleted) {
        this.name = name;
        this.isCompleted = isCompleted;
    }

    /**
     * Returns the icon associated with this task type
     *
     * @return The icon representing this task type
     */
    public abstract String getTypeIcon();

    /**
     * Converts this Task into its data string format, which may be saved to/loaded from a file
     *
     * @return The Task data in string format
     */
    public abstract String getDataString();

    @Override
    public String toString() {
        return "[" + getStatusMsg() + "][" + getTypeIcon() + "] " + name;
    }

    /**
     * Returns the indicator corresponding to the task's "completed" status
     *
     * @return The "completed" status indicator
     */
    private String getStatusMsg() {
        return isCompleted() ? "X" : " ";
    }

    /**
     * Returns whether the Task is completed
     *
     * @return Whether the Task is completed
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Toggles the Task's completed status
     */
    public void toggleComplete() {
        isCompleted = !isCompleted;
    }

    /**
     * Sets the Task as completed
     */
    public void complete() {
        isCompleted = true;
    }

    /**
     * Sets the Task as incomplete
     */
    public void undo() {
        isCompleted = false;
    }

    /**
     * Returns the respective Task object according to its type given the data string.
     * Each Task's data string must match the format returned from its getDataString() method
     *
     * @param dataStr The data string
     * @return The Task instance
     * @throws StorageException If the data string contains an invalid type or data format
     */
    public static Task createTaskFromString(String dataStr) throws StorageException {
        String[] dataParts = dataStr.split(DATA_SPLITTER);
        if (dataParts.length == 0) {
            throw new StorageException("Unable to detect Task type");
        }

        switch (dataParts[0].toUpperCase()) {
        case "T":
            return ToDo.createTaskFromDataParts(dataParts);
        case "D":
            return Deadline.createTaskFromDataParts(dataParts);
        case "E":
            return Event.createTaskFromDataParts(dataParts);
        default:
            throw new StorageException("Unknown task type \"" + dataParts[0] + "\"");
        }
    }

}
