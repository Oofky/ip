package bogos;

/**
 * Represents a to-do task.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete to-do task with the given description.
     *
     * @param description Description of the to-do.
     * @throws IllegalArgumentException If the description is blank.
     */
    public Todo(String description) {
        super("T", description);
    }
}
