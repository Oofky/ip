package bogos;

/** Represents a task that has a description but no associated date. */
public class Todo extends Task {
    /** Creates an incomplete to-do task with the given description. */
    public Todo(String description) {
        super("T", description);
    }
}
