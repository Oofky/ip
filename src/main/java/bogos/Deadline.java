package bogos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Represents a task that must be completed by a specified date. */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate by;

    /** Creates an incomplete deadline with the given description and due date. */
    public Deadline(String description, LocalDate by) {
        super("D", description);
        this.by = by;
    }

    /** Returns this deadline's due date. */
    public LocalDate getBy() {
        return by;
    }

    /** Returns the due date in the format used by the console UI. */
    public String getFormattedBy() {
        return by.format(DISPLAY_DATE_FORMATTER);
    }

    /** Returns this deadline in its user-facing display format. */
    @Override
    public String toString() {
        return super.toString() 
            + " (by: " + getFormattedBy() + ")"; 
    }

    /** Returns this deadline in the format used by the data file. */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() 
            + " | " + getBy().toString();
    }
}
