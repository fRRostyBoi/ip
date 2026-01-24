package tasks;

public class ToDo extends Task {

    public ToDo(String name) {
        super(name);
    }

    @Override
    public String getTypeIcon() {
        return "✎";
    }

}
