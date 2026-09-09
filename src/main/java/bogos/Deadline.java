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
    private final LocalDate dueDate;

    /**
     * Creates an incomplete deadline with the given description and due date.
     *
     * @param description Description of the deadline.
     * @param dueDate Due date of the deadline.
     * @throws IllegalArgumentException If the description is blank.
     */
    public Deadline(String description, LocalDate dueDate) {
        super("D", description);
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Returns the due date in the format used by the console UI.
     *
     * @return Formatted due date.
     */
    public String getFormattedDueDate() {
        return dueDate.format(DISPLAY_DATE_FORMATTER);
    }

    /**
     * Returns this deadline in its user-facing display format.
     *
     * @return User-facing deadline description.
     */
    @Override
    public String toString() {
        return super.toString()
                + " (by: " + getFormattedDueDate() + ")";
    }

    /**
     * Returns this deadline in the format used by the data file.
     *
     * @return Data-file representation of this deadline.
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat()
                + " | " + getDueDate();
    }
}
