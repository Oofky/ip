import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDate by;

    public Deadline(String description, LocalDate by) {
        super("D", description);
        this.by = by;
    }

    public String getBy() {
        return by.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
    } 

    @Override
    public String toString() {
        return super.toString() + " (by: " + getBy() + ")"; 
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + getBy();
    }
}
