import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Interprets user command text and converts it into application data. */
public class Parser {
    /** Creates the task described by a todo, deadline, or event command. */
    public Task parseTask(String command) throws BogosException {
        if (command.startsWith("todo ")) {
            return new Todo(getRequiredText(command.substring("todo ".length())));
        }
        if (command.startsWith("deadline ")) {
            return parseDeadline(command);
        }
        if (command.startsWith("event ")) {
            return parseEvent(command);
        }
        throw new BogosException("bwhat");
    }

    /** Parses a one-based task number from user input. */
    public int parseTaskNumber(String taskNumberText) throws BogosException {
        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new BogosException("Bogus. Bring Bogos base-ten. :[");
        }
    }

    /** Parses a deadline command into a deadline task. */
    private Task parseDeadline(String command) throws BogosException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex < "deadline ".length()) {
            throw new BogosException("bwhat [deadline ... /by ...]");
        }

        String description = getRequiredText(command.substring("deadline ".length(), byIndex));
        String by = getRequiredText(command.substring(byIndex + " /by ".length()));
        return new Deadline(description, parseDate(by));
    }

    /** Parses an event command into an event task. */
    private Task parseEvent(String command) throws BogosException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex < "event ".length() || toIndex < fromIndex) {
            throw new BogosException("bwhat [event ... /from ... /to ...]");
        }

        String description = getRequiredText(command.substring("event ".length(), fromIndex));
        String starting = getRequiredText(command.substring(fromIndex + " /from ".length(), toIndex));
        String ending = getRequiredText(command.substring(toIndex + " /to ".length()));
        try {
            return new Event(description, parseDate(starting), parseDate(ending));
        } catch (IllegalArgumentException e) {
            throw new BogosException("Bro be breathing backwards??");
        }
    }

    /** Returns non-blank command text after removing surrounding whitespace. */
    private String getRequiredText(String text) throws BogosException {
        String trimmedText = text.trim();
        if (trimmedText.isBlank()) {
            throw new BogosException("bwhat body");
        }
        return trimmedText;
    }

    /** Parses an ISO-8601 date and converts failures to a user-facing error. */
    private LocalDate parseDate(String dateText) throws BogosException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new BogosException("bwhat [yyyy-mm-dd]");
        }
    }
}
