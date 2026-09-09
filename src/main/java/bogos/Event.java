package bogos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents an event task.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate starting;
    private final LocalDate ending;

    /**
     * Creates an incomplete event with the given description and date range.
     *
     * @param description Description of the event.
     * @param starting Date on which the event starts.
     * @param ending Date on which the event ends.
     * @throws IllegalArgumentException If the description is blank or the end precedes the start.
     */
    public Event(String description, LocalDate starting, LocalDate ending) {
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

    /**
     * Returns the start date in the format used by the console UI.
     *
     * @return Formatted start date.
     */
    public String getFormattedStarting() {
        return starting.format(DISPLAY_DATE_FORMATTER);
    } 

    /**
     * Returns the end date in the format used by the console UI.
     *
     * @return Formatted end date.
     */
    public String getFormattedEnding() {
        return ending.format(DISPLAY_DATE_FORMATTER);
    }

    /**
     * Returns this event in its user-facing display format.
     *
     * @return User-facing event description.
     */
    @Override
    public String toString() {
        return super.toString() 
            + " (from: " + getFormattedStarting() 
            + " to: " + getFormattedEnding() + ")";
    }

    /**
     * Returns this event in the format used by the data file.
     *
     * @return Data-file representation of this event.
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() 
            + " | " + getStarting().toString() 
            + " | " + getEnding().toString();
    }
}
