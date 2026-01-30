package notjippity.commands;

import notjippity.tasks.TaskTracker;
import notjippity.io.Ui;
import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Task;
import notjippity.tasks.ToDo;

/**
 * Handles "ToDo" command logic and behaviour
 */
public class ToDoCmd extends Command {

    static final String FORMAT_CMD = "Format: todo <Name>";

    private Ui ui;
    private TaskTracker taskTracker;

    /**
     * Returns a new ToDoCmd instance
     *
     * @param ui The bot's UI
     * @param taskTracker The bot's task tracker
     */
    public ToDoCmd(Ui ui, TaskTracker taskTracker) {
        super("todo");
        this.ui = ui;
        this.taskTracker = taskTracker;
    }

    /**
     * Adds a ToDo task into the tasklist and executes feedback
     *
     * @param argStr User's input command arguments
     * @throws MissingArgException If user input is missing any arguments"
     */
    @Override
    public void execute(String cmdStr, String argStr) throws MissingArgException {
        // If the task name is empty
        if (argStr == null || argStr.trim().isEmpty()) {
            throw new MissingArgException("Sooo... what's this task called? (" + FORMAT_CMD + ")");
        }

        Task task = new ToDo(argStr);
        taskTracker.addTask(task);
        ui.send("++ " + task + " (" + taskTracker.getSize() + " total)");
    }

}
