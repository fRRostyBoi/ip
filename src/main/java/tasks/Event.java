package tasks;

import exceptions.StorageException;

public class Event extends Task {

    private String fromDate, toDate;

    public Event(String name, String fromDate, String toDate) {
        super(name);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    private Event(String name, boolean completed, String fromDate, String toDate) {
        super(name, completed);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String getDataString() {
        return getTypeIcon() + DATA_SEPARATOR + name + DATA_SEPARATOR + (completed ? "Y" : "N")
                       + DATA_SEPARATOR + fromDate + DATA_SEPARATOR + toDate;
    }

    @Override
    public String toString() {
        return super.toString() + " [" + fromDate + " - " + toDate + "]";
    }

    /**
     * Converts data part strings into a Event task
     * @param dataParts The data parts from Task.createTaskFromString
     * @return The Event Task
     * @throws StorageException If any data part is of an invalid format
     */
    public static Event createTaskFromDataParts(String[] dataParts) throws StorageException {
        if (dataParts.length < 5) {
            throw new StorageException("Insufficient arguments; expected 5 but found" + dataParts.length);
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

        String fromDateStr = dataParts[3],
               toDateStr = dataParts[4];
        if (fromDateStr.isEmpty()) {
            throw new StorageException("Invalid argument #4; expected FromDate but found empty string");
        }
        if (toDateStr.isEmpty()) {
            throw new StorageException("Invalid argument #5; expected ToDate but found empty string");
        }

        return new Event(name, status, fromDateStr, toDateStr);
    }

}
