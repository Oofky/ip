package bogos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Represents a task that takes place from one date through another date. */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate starting;
    private final LocalDate ending;

    /** Creates an incomplete event with the given description and date range. */
    public Event(String description, LocalDate starting, LocalDate ending) {
        super("E", description);
        if (ending.isBefore(starting)) {
            throw new IllegalArgumentException("Event end date cannot be before its start date.");
        }
        this.starting = starting;
        this.ending = ending;
    }

    /** Returns the date on which this event starts. */
    public LocalDate getStarting() {
        return starting;
    }

    /** Returns the date on which this event ends. */
    public LocalDate getEnding() {
        return ending;
    }

    /** Returns the start date in the format used by the console UI. */
    public String getFormattedStarting() {
        return starting.format(DISPLAY_DATE_FORMATTER);
    } 

    /** Returns the end date in the format used by the console UI. */
    public String getFormattedEnding() {
        return ending.format(DISPLAY_DATE_FORMATTER);
    }

    /** Returns this event in its user-facing display format. */
    @Override
    public String toString() {
        return super.toString() 
            + " (from: " + getFormattedStarting() 
            + " to: " + getFormattedEnding() + ")";
    }

    /** Returns this event in the format used by the data file. */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() 
            + " | " + getStarting().toString() 
            + " | " + getEnding().toString();
    }
}
