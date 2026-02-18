package notjippity.io;

import java.util.List;

import notjippity.exceptions.StorageException;
import notjippity.tasks.Task;
import notjippity.utils.StorageDataLoader;

/**
 * Represents the bot's persistent data storage system for tasks.
 */
public class TaskStorage extends Storage {

    private static final String REL_FILE_PATH = "data/tasks.txt";

    /**
     * Returns a new TaskStorage instance.
     */
    public TaskStorage() {
        super(REL_FILE_PATH);
    }

    /**
     * Attempts to parse the task data contained in the file into a collection of Tasks.
     *
     * @return The list of tasks loaded from file.
     * @throws StorageException If the file content is of the wrong format/corrupted.
     */
    public List<Task> loadTasks() throws StorageException {
        return StorageDataLoader.loadDataWithParser(loadData(), Task::createTaskFromString);
    }

    /**
     * Saves all Tasks to file with the provided List of data strings.
     *
     * @param dataStrings The list of data strings given by TaskTracker.getAllDataStrings().
     * @throws StorageException If an I/O error occurs during the saving process.
     */
    public void saveTasks(List<String> dataStrings) throws StorageException {
        saveData(dataStrings);
    }

}
