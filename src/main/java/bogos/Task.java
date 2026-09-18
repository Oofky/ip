package bogos;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task with a type, description, and completion state.
 */
public abstract class Task {
    protected static final DateTimeFormatter DISPLAY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final TaskType taskType;
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task of the supplied type and description.
     *
     * @param taskType Type of this task.
     * @param description Description of the task.
     * @throws IllegalArgumentException If the description is blank.
     */
    protected Task(TaskType taskType, String description) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.taskType = taskType;
        this.description = description;
        this.isDone = false;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getDescription() {
        return description;
    }

    public String getStatusIcon() {
        return (isDone() ? "X" : " ");
    }

    /**
     * Marks this task as complete.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
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
        return "[" + getTaskType().getStorageCode() + "][" + getStatusIcon() + "] "
                + getDescription();
    }

    /**
     * Returns this task in the format used by the data file.
     *
     * @return Data-file representation of this task.
     */
    public String toFileFormat() {
        return getTaskType().getStorageCode()
                + " | " + (isDone() ? "true" : "false")
                + " | " + getDescription();
    }
}
