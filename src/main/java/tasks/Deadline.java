package tasks;

import exceptions.StorageException;

public class Deadline extends Task {

    private String byDate;

    public Deadline(String name, String byDate) {
        super(name);
        this.byDate = byDate;
    }

    private Deadline(String name, boolean completed, String byDate) {
        super(name, completed);
        this.byDate = byDate;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String getDataString() {
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (completed ? "Y" : "N") + DATA_SEPARATOR + byDate;
    }

    @Override
    public String toString() {
        return super.toString() + " [" + byDate + "]";
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
        if (byDateStr.isEmpty()) {
            throw new StorageException("Invalid argument #4; expected ByDate but found empty string");
        }

        return new Deadline(name, status, byDateStr);
    }

}
