package bogos;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Interprets user command text and converts it into application data.
 */
public class Parser {
    private static final String TODO_COMMAND_PREFIX = "todo ";
    private static final String DEADLINE_COMMAND_PREFIX = "deadline ";
    private static final String EVENT_COMMAND_PREFIX = "event ";
    private static final String DEADLINE_DATE_MARKER = " /by ";
    private static final String EVENT_START_DATE_MARKER = " /from ";
    private static final String EVENT_END_DATE_MARKER = " /to ";

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
        ParsedTaskInput parsedInput = extractTags(command);
        String commandWithoutTags = parsedInput.commandWithoutTags();
        List<String> tags = parsedInput.tags();

        if (commandWithoutTags.equals("todo") || commandWithoutTags.startsWith(TODO_COMMAND_PREFIX)) {
            return new Todo(getRequiredText(commandWithoutTags.substring("todo".length())), tags);
        }
        if (commandWithoutTags.startsWith(DEADLINE_COMMAND_PREFIX)) {
            return parseDeadline(commandWithoutTags, tags);
        }
        if (commandWithoutTags.startsWith(EVENT_COMMAND_PREFIX)) {
            return parseEvent(commandWithoutTags, tags);
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
     * @param tags Tags assigned to the deadline.
     * @return Deadline task described by the command.
     * @throws BogosException If the command is invalid.
     */
    private Task parseDeadline(String command, List<String> tags) throws BogosException {
        assert command.startsWith("deadline ")
                : "Deadline parsing is only reached for deadline commands.";
        int byIndex = command.indexOf(DEADLINE_DATE_MARKER);
        if (byIndex < DEADLINE_COMMAND_PREFIX.length()) {
            throw new BogosException("bwhat [deadline ... /by ...]");
        }

        String description = getRequiredText(command.substring(DEADLINE_COMMAND_PREFIX.length(), byIndex));
        String by = getRequiredText(command.substring(byIndex + DEADLINE_DATE_MARKER.length()));
        return new Deadline(description, parseDate(by), tags);
    }

    /**
     * Parses an event command into an event task.
     *
     * @param command Event command to parse.
     * @param tags Tags assigned to the event.
     * @return Event task described by the command.
     * @throws BogosException If the command is invalid or its dates are reversed.
     */
    private Task parseEvent(String command, List<String> tags) throws BogosException {
        assert command.startsWith("event ")
                : "Event parsing is only reached for event commands.";
        int fromIndex = command.indexOf(EVENT_START_DATE_MARKER);
        int toIndex = command.indexOf(EVENT_END_DATE_MARKER);
        if (fromIndex < EVENT_COMMAND_PREFIX.length() || toIndex < fromIndex) {
            throw new BogosException("bwhat [event ... /from ... /to ...]");
        }

        String description = getRequiredText(command.substring(EVENT_COMMAND_PREFIX.length(), fromIndex));
        String starting = getRequiredText(command.substring(fromIndex + EVENT_START_DATE_MARKER.length(), toIndex));
        String ending = getRequiredText(command.substring(toIndex + EVENT_END_DATE_MARKER.length()));
        try {
            return new Event(description, parseDate(starting), parseDate(ending), tags);
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

    /**
     * Separates inline tag tokens from a task command.
     *
     * @param command Raw task command.
     * @return Task command without tags and tags in their original order.
     * @throws BogosException If a tag is blank or duplicated.
     */
    private ParsedTaskInput extractTags(String command) throws BogosException {
        String[] tokens = command.split("\\s+");
        List<String> commandTokens = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        Set<String> uniqueTags = new HashSet<>();
        boolean hasTags = false;

        for (String token : tokens) {
            if (!token.startsWith("#")) {
                commandTokens.add(token);
                continue;
            }

            hasTags = true;
            String tag = token.substring(1);
            if (tag.isEmpty()) {
                throw new BogosException("bwhat tag");
            }
            if (!uniqueTags.add(tag)) {
                throw new BogosException("bwhat duplicate tag");
            }
            tags.add(tag);
        }
        String commandWithoutTags = hasTags ? String.join(" ", commandTokens) : command;
        return new ParsedTaskInput(commandWithoutTags, tags);
    }

    /**
     * Holds a task command after tag extraction.
     *
     * @param commandWithoutTags Task command with inline tags removed.
     * @param tags Tags extracted from the command.
     */
    private record ParsedTaskInput(String commandWithoutTags, List<String> tags) {
    }
}
