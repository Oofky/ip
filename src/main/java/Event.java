public class Event extends Task {
    private final String starting;
    private final String ending;

    public Event(String description, String starting, String ending) {
        super("E", description);
        this.starting = starting;
        this.ending = ending;
    }

    public String getStarting() {
        return starting;
    } 

    public String getEnding() {
        return ending;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + getStarting() + " to: " + getEnding() + ")";
    }

    public String toFileFormat() {
        return super.toFileFormat() + " | " + getStarting() + " | " + getEnding();
    }
}
