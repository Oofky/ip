package bogos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a deadline task.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate by;

    /**
     * Creates an incomplete deadline with the given description and due date.
     *
     * @param description Description of the deadline.
     * @param by Due date of the deadline.
     * @throws IllegalArgumentException If the description is blank.
     */
    public Deadline(String description, LocalDate by) {
        super("D", description);
        this.by = by;
    }

    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns the due date in the format used by the console UI.
     *
     * @return Formatted due date.
     */
    public String getFormattedBy() {
        return by.format(DISPLAY_DATE_FORMATTER);
    }

    /**
     * Returns this deadline in its user-facing display format.
     *
     * @return User-facing deadline description.
     */
    @Override
    public String toString() {
        return super.toString() 
            + " (by: " + getFormattedBy() + ")"; 
    }

    /**
     * Returns this deadline in the format used by the data file.
     *
     * @return Data-file representation of this deadline.
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() 
            + " | " + getBy().toString();
    }
}
