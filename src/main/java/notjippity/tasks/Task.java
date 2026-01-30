package notjippity.tasks;

import notjippity.exceptions.StorageException;

public abstract class Task {

    protected static final String DATA_SEPARATOR = "||";
    protected static final String DATA_SPLITTER = "\\|\\|";

    protected String name;
    protected boolean isCompleted;

    protected Task(String name) {
        this.name = name;
    }

    protected Task(String name, boolean isCompleted) {
        this.name = name;
        this.isCompleted = isCompleted;
    }

    public abstract String getTypeIcon();

    /**
     * Converts this Task into its data string format, which may be saved to/loaded from a file
     * @return The data string
     */
    public abstract String getDataString();

    @Override
    public String toString() {
        return "[" + getStatusMsg() + "][" + getTypeIcon() + "] " + name;
    }

    private String getStatusMsg() {
        return isCompleted() ? "X" : " ";
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void toggleComplete() {
        isCompleted = !isCompleted;
    }

    public void complete() {
        isCompleted = true;
    }

    public void undo() {
        isCompleted = false;
    }

    /**
     * Parses a data string and returns the respective Task object according to its type
     * @param dataStr The data string
     * @return The Task object
     * @throws StorageException If the data string contains an invalid type or data format
     */
    public static Task createTaskFromString(String dataStr) throws StorageException {
        String[] dataParts = dataStr.split(DATA_SPLITTER);
        if (dataParts.length == 0) {
            throw new StorageException("Unable to detect file type");
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
