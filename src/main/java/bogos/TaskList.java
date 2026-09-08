package bogos;

import java.util.ArrayList;
import java.util.List;

/** Stores and manages the tasks currently known to the application. */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Creates a task list containing copies of the supplied task references. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Returns whether this task list contains no tasks. */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Returns the number of tasks in this list. */
    public int size() {
        return tasks.size();
    }

    /** Adds a task to the end of this list. */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /** Returns the task identified by its one-based number. */
    public Task getTask(int taskNumber) throws BogosException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BogosException("Bummer. Bullet beyond bounds. :[");
        }
        return tasks.get(taskNumber - 1);
    }

    /** Removes and returns the task identified by its one-based number. */
    public Task removeTask(int taskNumber) throws BogosException {
        Task task = getTask(taskNumber);
        tasks.remove(task);
        return task;
    }

    /** Returns an unmodifiable snapshot of the tasks for display or saving. */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
