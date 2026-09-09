package bogos;

/**
 * Represents a task with a type, description, and completion state.
 */
public abstract class Task {
    private final String taskType;
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task of the supplied type and description.
     *
     * @param taskType Single-letter identifier for the task type.
     * @param description Description of the task.
     * @throws IllegalArgumentException If the description is blank.
     */
    protected Task(String taskType, String description) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.taskType = taskType;
        this.description = description;
        this.isDone = false;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getDescription() {
        return description;
    }

    public String getStatusIcon() {
        return (isDone() ? "X" : " ");
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns this task in its user-facing display format.
     *
     * @return User-facing task description.
     */
    @Override
    public String toString() {
        return "[" + getTaskType() + "][" + getStatusIcon() + "] " 
            + getDescription(); 
    }

    /**
     * Returns this task in the format used by the data file.
     *
     * @return Data-file representation of this task.
     */
    public String toFileFormat() {
        return getTaskType() 
            + " | " + (isDone() ? "true" : "false")
            + " | " + getDescription();
    }
}
