package bogos;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the tasks currently known to the application.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing copies of the supplied task references.
     *
     * @param tasks Tasks with which to initialise the list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds a task to this list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task identified by its one-based number.
     *
     * @param taskNumber One-based position of the task.
     * @return The requested task.
     * @throws BogosException If the task number is outside the list.
     */
    public Task getTask(int taskNumber) throws BogosException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BogosException("Bummer. Bullet beyond bounds. :[");
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes and returns the task identified by its one-based number.
     *
     * @param taskNumber One-based position of the task.
     * @return The removed task.
     * @throws BogosException If the task number is outside the list.
     */
    public Task removeTask(int taskNumber) throws BogosException {
        Task task = getTask(taskNumber);
        tasks.remove(task);
        return task;
    }

    /**
     * Returns an unmodifiable snapshot of the tasks for display or saving.
     *
     * @return Snapshot of the tasks in this list.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
