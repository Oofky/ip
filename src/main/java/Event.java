import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private final LocalDate starting;
    private final LocalDate ending;

    public Event(String description, LocalDate starting, LocalDate ending) {
        super("E", description);
        this.starting = starting;
        this.ending = ending;
    }

    public String getStarting() {
        return starting.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    } 

    public String getEnding() {
        return ending.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + getStarting() + " to: " + getEnding() + ")";
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + getStarting() + " | " + getEnding();
    }
}
