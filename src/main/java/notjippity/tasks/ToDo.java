package notjippity.tasks;

import notjippity.exceptions.StorageException;
import notjippity.utils.TaskDataParser;

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
     * Returns a new ToDo instance.
     *
     * @param name        The task name.
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

        TaskDataParser.validateDataPartsLength(dataParts, 3);

        String name = TaskDataParser.parseName(dataParts[1]);
        boolean isCompleted = TaskDataParser.parseCompletionStatus(dataParts[2]);

        return new ToDo(name, isCompleted);
    }

}
