public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super("D", description);
        this.by = by;
    }

    public String getBy() {
        return by;
    } 

    @Override
    public String toString() {
        return super.toString() + " (by: " + getBy() + ")"; 
    }
}
