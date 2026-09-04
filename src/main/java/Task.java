public class Task {
    public final String taskType;
    private final String description;
    private boolean isDone;

    public Task(String taskType, String description) {
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

    @Override
    public String toString() {
        return "[" + getTaskType() + "][" + getStatusIcon() + "] " + getDescription(); 
    }

    public String toFileFormat() {
        return getTaskType() + " | " + (isDone() ? "1" : "0") + " | " + getDescription();
    }
}
