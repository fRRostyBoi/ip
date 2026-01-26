package tasks;

public abstract class Task {

    protected String name;
    protected boolean completed;

    protected Task(String name) {
        this.name = name;
    }

    public abstract String getTypeIcon();

    @Override
    public String toString() {
        return "[" + getStatusMsg() + "][" + getTypeIcon() + "] " + name;
    }

    private String getStatusMsg() {
        return isCompleted() ? "X" : " ";
    }

    public boolean isCompleted() {
        return completed;
    }

    public void toggleComplete() {
        completed = !completed;
    }

    public void complete() {
        completed = true;
    }

    public void undo() {
        completed = false;
    }

}
