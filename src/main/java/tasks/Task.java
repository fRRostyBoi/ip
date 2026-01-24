package tasks;

public class Task {

    protected String name;
    protected boolean completed;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[" + getStatusMsg() + "] " + name;
    }

    private String getStatusMsg() {
        return completed ? "✓" : " ";
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
