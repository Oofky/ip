package bogos;

/** Represents a task with a type, description, and completion state. */
public abstract class Task {
    private final String taskType;
    private final String description;
    private boolean isDone;

    /** Creates an incomplete task of the supplied type and description. */
    protected Task(String taskType, String description) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.taskType = taskType;
        this.description = description;
        this.isDone = false;
    }

    /** Returns this task's single-letter type identifier. */
    public String getTaskType() {
        return taskType;
    }

    /** Returns this task's description. */
    public String getDescription() {
        return description;
    }

    /** Returns the icon representing this task's completion state. */
    public String getStatusIcon() {
        return (isDone() ? "X" : " ");
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /** Returns whether this task is complete. */
    public boolean isDone() {
        return isDone;
    }

    /** Returns this task in its user-facing display format. */
    @Override
    public String toString() {
        return "[" + getTaskType() + "][" + getStatusIcon() + "] " 
            + getDescription(); 
    }

    /** Returns this task in the format used by the data file. */
    public String toFileFormat() {
        return getTaskType() 
            + " | " + (isDone() ? "true" : "false")
            + " | " + getDescription();
    }
}
