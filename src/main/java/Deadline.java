import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate by;

    public Deadline(String description, LocalDate by) {
        super("D", description);
        this.by = by;
    }

    public LocalDate getBy() {
        return by;
    }

    public String getFormattedBy() {
        return by.format(DISPLAY_DATE_FORMATTER);
    }

    @Override
    public String toString() {
        return super.toString() 
            + " (by: " + getFormattedBy() + ")"; 
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() 
            + " | " + getBy().toString();
    }
}
