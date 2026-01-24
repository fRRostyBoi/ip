package tasks;

public class Event extends Task {

    private String fromDate, toDate;

    public Event(String name, String fromDate, String toDate) {
        super(name);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " [" + fromDate + " - " + toDate + "]";
    }

}
