package file;

import exceptions.StorageException;
import tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides functions to maintain bot persistent data (i.e. loading/saving data to/from file
 */
public class Storage {

    private static final String REL_FILE_PATH = "data/tasks.txt";

    private Ui ui;
    private File file;
    private Scanner fileReader;

    public Storage(Ui ui) {
        this.ui = ui;
        file = new File(REL_FILE_PATH);
    }

    /**
     * Triggers startup behaviour of the Storage. Must be called first before accessing
     * any Storage functions (e.g. load/save data). Terminates the bot immediately if
     * any storage exceptions (corrupted file, invalid file content format) is detected
     */
    public void init() {
        try {
            loadFile();
            fileReader = new Scanner(file);
        } catch (StorageException | FileNotFoundException exception) {
            ui.sendRaw(exception.getMessage());
            System.exit(1);
        }
    }

    /**
     * Creates the data file if it does not exist, along with any parent folders along the way
     * @throws StorageException If an I/O exception occurs while creating the data file
     */
    private void loadFile() throws StorageException {
        // If file does not exist, ensure the parent (and any ancestor(s) above) folder is created, then creates the file
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                // If file somehow already exists, fails silently
                if (file.createNewFile()) {
                    ui.sendRaw("Data file not detected, created new file");
                }
            } catch (IOException exception) {
                throw new StorageException("An I/o error occured while trying to create the file, exiting...");
            }
        }
    }

    /**
     * Attempts to load, parse and return the task data contained in the file
     * @throws StorageException If the file content is of the wrong format/corrupted
     */
    public List<Task> loadData() throws StorageException {
        ArrayList<Task> tasks = new ArrayList<>();

        // For each line in the file, parse each line and form the Task
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
     * Saves all Tasks to file
     * @param tasks The list of all tasks
     * @throws StorageException If an I/O error occurs during the saving process
     */
    public void saveData(List<Task> tasks) throws StorageException {
        try {
            FileWriter fileWriter = new FileWriter(REL_FILE_PATH);
            for (int i = 0; i < tasks.size(); i++) {
                if (i > 0) {
                    fileWriter.write(System.lineSeparator());
                }
                fileWriter.write(tasks.get(i).getDataString());
            }

            fileWriter.close();
        } catch (IOException exception) {
            throw new StorageException("An error occurred while saving data to file");
        }
    }

}
