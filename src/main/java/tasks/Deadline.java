package tasks;

public class Deadline extends Task {

    private String byDate;

    public Deadline(String name, String byDate) {
        super(name);
        this.byDate = byDate;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " [" + byDate + "]";
    }

}
