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
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * Creates an incomplete event with the given description and date range.
     *
     * @param description Description of the event.
     * @param startDate Date on which the event starts.
     * @param endDate Date on which the event ends.
     * @throws IllegalArgumentException If the description is blank or the end precedes the start.
     */
    public Event(String description, LocalDate startDate, LocalDate endDate) {
        super("E", description);
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Event end date cannot be before its start date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the start date in the format used by the console UI.
     *
     * @return Formatted start date.
     */
    public String getFormattedStartDate() {
        return startDate.format(DISPLAY_DATE_FORMATTER);
    }

    /**
     * Returns the end date in the format used by the console UI.
     *
     * @return Formatted end date.
     */
    public String getFormattedEndDate() {
        return endDate.format(DISPLAY_DATE_FORMATTER);
    }

    /**
     * Returns this event in its user-facing display format.
     *
     * @return User-facing event description.
     */
    @Override
    public String toString() {
        return super.toString()
                + " (from: " + getFormattedStartDate()
                + " to: " + getFormattedEndDate() + ")";
    }

    /**
     * Returns this event in the format used by the data file.
     *
     * @return Data-file representation of this event.
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat()
                + " | " + getStartDate()
                + " | " + getEndDate();
    }
}
