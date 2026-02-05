package notjippity.commands;

import java.util.List;

import notjippity.exceptions.MissingArgException;
import notjippity.tasks.Task;
import notjippity.tasks.TaskTracker;
import notjippity.tasks.ToDo;

/**
 * Handles "ToDo" command logic and behaviour.
 */
public class ToDoCmd extends Command {

    static final String FORMAT_CMD = "Format: todo <Name>";

    private TaskTracker taskTracker;

    /**
     * Returns a new ToDoCmd instance.
     *
     * @param taskTracker The bot's task tracker.
     */
    public ToDoCmd(TaskTracker taskTracker) {
        super("todo");
        this.taskTracker = taskTracker;
    }

    /**
     * Adds a ToDo task into the tasklist.
     *
     * @param argStr User's input command arguments.
     * @return The bot's response.
     * @throws MissingArgException If user input is missing any arguments.
     */
    @Override
    public List<String> execute(String cmdStr, String argStr) throws MissingArgException {
        // If the task name is empty
        if (argStr == null || argStr.trim().isEmpty()) {
            throw new MissingArgException("Sooo... what's this task called? (" + FORMAT_CMD + ")");
        }

        Task task = new ToDo(argStr);
        taskTracker.addTask(task);
        return List.of("++ " + task + " (" + taskTracker.getSize() + " total)");
    }

}
