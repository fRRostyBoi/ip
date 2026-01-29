package notjippity.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks all tasks and provides functions to maintain tasks
 */
public class TaskTracker {

    private ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Adds a Task into the list of .tasks
     * @param task The task to be added
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a Task from the list
     * @param task The task to remove
     */
    public void removeTask(Task task) {
        tasks.remove(task);
    }

    /**
     * Returns the list of .tasks
     * @return The list of .tasks
     */
    public List<Task> getTasks() {
        return (List<Task>) tasks.clone();
    }

    /**
     * Returns the size of the task list
     * @return The size of the task list
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Converts all Tasks into data string form and returns it as a
     * List in the same order as the original list
     * @return The list of data strings
     */
    public List<String> getAllDataStrings() {
        List<String> data = new ArrayList<>();
        for (Task task : tasks) {
            data.add(task.getDataString());
        }
        return data;
    }

    /**
     * Returns the task specified by the given index, 0-indexed
     * @param index The index of the task
     * @return The task specified by the given index
     * @throws IndexOutOfBoundsException If the provided index is outside the range of lists
     */
    public Task getTask(int index) throws IndexOutOfBoundsException {
        return tasks.get(index);
    }

}
