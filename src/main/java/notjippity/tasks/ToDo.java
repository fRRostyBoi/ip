package notjippity.tasks;

import notjippity.exceptions.StorageException;

public class ToDo extends Task {

    public ToDo(String name) {
        super(name);
    }

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
     * Converts data part strings into a ToDo task
     * @param dataParts The data parts from Task.createTaskFromString
     * @return The ToDo Task
     * @throws StorageException If any data part is of an invalid format
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
