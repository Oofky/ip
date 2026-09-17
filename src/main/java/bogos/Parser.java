package bogos;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Interprets user command text and converts it into application data.
 */
public class Parser {
    private static final String TODO_COMMAND_PREFIX = "todo ";
    private static final String DEADLINE_COMMAND_PREFIX = "deadline ";
    private static final String EVENT_COMMAND_PREFIX = "event ";

    /**
     * Returns whether a command creates a task.
     *
     * @param command Command to classify.
     * @return Whether the command is a to-do, deadline, or event command.
     */
    public boolean isTaskCommand(String command) {
        return command.startsWith(TODO_COMMAND_PREFIX)
                || command.startsWith(DEADLINE_COMMAND_PREFIX)
                || command.startsWith(EVENT_COMMAND_PREFIX);
    }

    /**
     * Creates the task described by a to-do, deadline, or event command.
     *
     * @param command Command describing the task.
     * @return Task described by the command.
     * @throws BogosException If the command is not a valid task command.
     */
    public Task parseTask(String command) throws BogosException {
        if (command.startsWith(TODO_COMMAND_PREFIX)) {
            return new Todo(getRequiredText(command.substring(TODO_COMMAND_PREFIX.length())));
        }
        if (command.startsWith(DEADLINE_COMMAND_PREFIX)) {
            return parseDeadline(command);
        }
        if (command.startsWith(EVENT_COMMAND_PREFIX)) {
            return parseEvent(command);
        }
        throw new BogosException("bwhat");
    }

    /**
     * Parses a one-based task number from user input.
     *
     * @param taskNumberText Text containing the task number.
     * @return Parsed task number.
     * @throws BogosException If the text is not a whole number.
     */
    public int parseTaskNumber(String taskNumberText) throws BogosException {
        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new BogosException("Bogus. Bring Bogos base-ten. :[");
        }
    }

    /**
     * Parses a deadline command into a deadline task.
     *
     * @param command Deadline command to parse.
     * @return Deadline task described by the command.
     * @throws BogosException If the command is invalid.
     */
    private Task parseDeadline(String command) throws BogosException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex < DEADLINE_COMMAND_PREFIX.length()) {
            throw new BogosException("bwhat [deadline ... /by ...]");
        }

        String description = getRequiredText(command.substring(DEADLINE_COMMAND_PREFIX.length(), byIndex));
        String by = getRequiredText(command.substring(byIndex + " /by ".length()));
        return new Deadline(description, parseDate(by));
    }

    /**
     * Parses an event command into an event task.
     *
     * @param command Event command to parse.
     * @return Event task described by the command.
     * @throws BogosException If the command is invalid or its dates are reversed.
     */
    private Task parseEvent(String command) throws BogosException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex < EVENT_COMMAND_PREFIX.length() || toIndex < fromIndex) {
            throw new BogosException("bwhat [event ... /from ... /to ...]");
        }

        String description = getRequiredText(command.substring(EVENT_COMMAND_PREFIX.length(), fromIndex));
        String starting = getRequiredText(command.substring(fromIndex + " /from ".length(), toIndex));
        String ending = getRequiredText(command.substring(toIndex + " /to ".length()));
        try {
            return new Event(description, parseDate(starting), parseDate(ending));
        } catch (IllegalArgumentException e) {
            throw new BogosException("Bro be breathing backwards??");
        }
    }

    /**
     * Returns non-blank command text after removing surrounding whitespace.
     *
     * @param text Text to validate and trim.
     * @return Trimmed non-blank text.
     * @throws BogosException If the text is blank.
     */
    private String getRequiredText(String text) throws BogosException {
        String trimmedText = text.trim();
        if (trimmedText.isBlank()) {
            throw new BogosException("bwhat body");
        }
        return trimmedText;
    }

    /**
     * Parses an ISO-8601 date and converts failures to a user-facing error.
     *
     * @param dateText Date text to parse.
     * @return Parsed date.
     * @throws BogosException If the date text is invalid.
     */
    private LocalDate parseDate(String dateText) throws BogosException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new BogosException("bwhat [yyyy-mm-dd]");
        }
    }
}
