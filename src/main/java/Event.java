import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate starting;
    private final LocalDate ending;

    public Event(String description, 
                LocalDate starting, 
                LocalDate ending) throws IllegalArgumentException {
        super("E", description);
        if (ending.isBefore(starting)) {
            throw new IllegalArgumentException("Event end date cannot be before its start date.");
        }
        this.starting = starting;
        this.ending = ending;
    }

    public LocalDate getStarting() {
        return starting;
    }

    public LocalDate getEnding() {
        return ending;
    }

    public String getFormattedStarting() {
        return starting.format(DISPLAY_DATE_FORMATTER);
    } 

    public String getFormattedEnding() {
        return ending.format(DISPLAY_DATE_FORMATTER);
    }

    @Override
    public String toString() {
        return super.toString() 
            + " (from: " + getFormattedStarting() 
            + " to: " + getFormattedEnding() + ")";
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() 
            + " | " + getStarting().toString() 
            + " | " + getEnding().toString();
    }
}
