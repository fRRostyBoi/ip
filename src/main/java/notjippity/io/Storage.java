package notjippity.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import notjippity.exceptions.StorageException;
import notjippity.tasks.Task;

/**
 * Represents the bot's persistent data storage system.
 */
public class Storage {

    private static final String REL_FILE_PATH = "data/tasks.txt";

    private final File file;
    private Scanner fileReader;

    /**
     * Returns a new Storage instance.
     */
    public Storage() {
        file = new File(REL_FILE_PATH);
    }

    /**
     * Runs the Storage startup sequence. Must be called once before calling any other Storage methods.
     *
     * @throws StorageException If an error occurs while loading the file or instantiating the File Scanner.
     */
    public void init() throws StorageException {
        loadFile();
        loadFileScanner();
    }

    /**
     * Creates the data file if it does not exist, along with any required parent folders.
     *
     * @throws StorageException If an I/O exception occurs while creating the data file.
     */
    private void loadFile() throws StorageException {
        // Ensure parent folders exist before creating the file (if it does not exist)
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                // Note that this fails silently (i.e. file does not exist a few lines before,
                // but apparently exists now, failing this conditional)
                if (file.createNewFile()) {
                    System.out.println("Data file not detected, created new file");
                }
            } catch (IOException exception) {
                throw new StorageException("An I/O error occured while trying to create the file, exiting...");
            }
        }
    }

    /**
     * Instantiates the file Scanner instance.
     *
     * @throws StorageException If the file is not found or cannot be accessed.
     */
    private void loadFileScanner() throws StorageException {
        try {
            fileReader = new Scanner(file);
        } catch (FileNotFoundException exception) {
            throw new StorageException("Unable to find file while loading it into scanner!");
        }
    }

    /**
     * Attempts to parse the task data contained in the file into a collection of Tasks.
     *
     * @return The list of tasks loaded from file.
     * @throws StorageException If the file content is of the wrong format/corrupted.
     */
    public List<Task> loadData() throws StorageException {
        ArrayList<Task> tasks = new ArrayList<>();

        // Reads each line in the file and parse them into a Task instance
        int index = 1;
        while (fileReader.hasNext()) {
            String dataStr = fileReader.nextLine();

            try {
                Task task = Task.createTaskFromString(dataStr);
                tasks.add(task);
            } catch (StorageException exception) {
                throw new StorageException("Invalid file format on line " + index + ": " + exception.getMessage());
            }

            index++;
        }

        return tasks;
    }

    /**
     * Saves all Tasks to file with the provided List of data strings.
     *
     * @param dataStrings The list of data strings given by TaskTracker.getAllDataStrings().
     * @throws StorageException If an I/O error occurs during the saving process.
     */
    public void saveData(List<String> dataStrings) throws StorageException {
        try {
            FileWriter fileWriter = new FileWriter(REL_FILE_PATH);

            for (int i = 0; i < dataStrings.size(); i++) {
                if (i > 0) {
                    fileWriter.write(System.lineSeparator());
                }
                fileWriter.write(dataStrings.get(i));
            }

            fileWriter.close();
        } catch (IOException exception) {
            throw new StorageException("An error occurred while saving data to file");
        }
    }

}
