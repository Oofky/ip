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
    private static final String TODO_BODY_ERROR = "Bwhere body? Be: todo DESCRIPTION [#TAG]...";
    private static final String DEADLINE_BODY_ERROR =
            "Bwhere body? Be: deadline DESCRIPTION /by YYYY-MM-DD [#TAG]...";
    private static final String EVENT_BODY_ERROR =
            "Bwhere body? Be: event DESCRIPTION /from YYYY-MM-DD /to YYYY-MM-DD [#TAG]...";
    private static final String DEADLINE_BY_ERROR =
            "Bwhere /by? Be: deadline DESCRIPTION /by YYYY-MM-DD [#TAG]...";
    private static final String EVENT_FROM_ERROR =
            "Bwhere /from? Be: event DESCRIPTION /from YYYY-MM-DD /to YYYY-MM-DD [#TAG]...";
    private static final String EVENT_TO_ERROR =
            "Bwhere /to? Be: event DESCRIPTION /from YYYY-MM-DD /to YYYY-MM-DD [#TAG]...";

    /**
     * Returns whether a command creates a task.
     *
     * @param command Command to classify.
     * @return Whether the command is a to-do, deadline, or event command.
     */
    public boolean isTaskCommand(String command) {
        return command.equals("todo")
                || command.startsWith(TODO_COMMAND_PREFIX)
                || command.equals("deadline")
                || command.startsWith(DEADLINE_COMMAND_PREFIX)
                || command.equals("event")
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
        if (command.equals("todo")) {
            throw new BogosException(TODO_BODY_ERROR);
        }
        if (command.equals("deadline")) {
            throw new BogosException(DEADLINE_BODY_ERROR);
        }
        if (command.equals("event")) {
            throw new BogosException(EVENT_BODY_ERROR);
        }

        ParsedTaskInput parsedInput = extractTags(command);
        String commandWithoutTags = parsedInput.commandWithoutTags();
        List<String> tags = parsedInput.tags();

        if (commandWithoutTags.equals("todo") || commandWithoutTags.startsWith(TODO_COMMAND_PREFIX)) {
            return new Todo(getRequiredTodoDescription(commandWithoutTags.substring("todo".length())), tags);
        }
        if (commandWithoutTags.equals("deadline")) {
            throw new BogosException(DEADLINE_BODY_ERROR);
        }
        if (commandWithoutTags.startsWith(DEADLINE_COMMAND_PREFIX)) {
            return parseDeadline(commandWithoutTags, tags);
        }
        if (commandWithoutTags.equals("event")) {
            throw new BogosException(EVENT_BODY_ERROR);
        }
        if (commandWithoutTags.startsWith(EVENT_COMMAND_PREFIX)) {
            return parseEvent(commandWithoutTags, tags);
        }
        throw new BogosException("Bwhat? Best browse: help");
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
        String descriptionAndParameters = command.substring(DEADLINE_COMMAND_PREFIX.length());
        if (descriptionAndParameters.isBlank() || descriptionAndParameters.startsWith("/by")) {
            throw new BogosException(DEADLINE_BODY_ERROR);
        }
        if (countParameterOccurrences(command, "/by") > 1) {
            throw new BogosException("Bummer, buplicate /by. :[");
        }

        int byIndex = command.indexOf(DEADLINE_DATE_MARKER);
        if (byIndex < DEADLINE_COMMAND_PREFIX.length()) {
            throw new BogosException(DEADLINE_BY_ERROR);
        }

        String description = getRequiredDeadlineDescription(
                command.substring(DEADLINE_COMMAND_PREFIX.length(), byIndex));
        String by = command.substring(byIndex + DEADLINE_DATE_MARKER.length()).trim();
        if (by.isBlank()) {
            throw new BogosException(DEADLINE_BY_ERROR);
        }
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
        String descriptionAndParameters = command.substring(EVENT_COMMAND_PREFIX.length());
        if (descriptionAndParameters.isBlank() || descriptionAndParameters.startsWith("/from")
                || descriptionAndParameters.startsWith("/to")) {
            throw new BogosException(EVENT_BODY_ERROR);
        }
        if (countParameterOccurrences(command, "/from") > 1) {
            throw new BogosException("Bummer, buplicate /from. :[");
        }
        if (countParameterOccurrences(command, "/to") > 1) {
            throw new BogosException("Bummer, buplicate /to. :[");
        }

        int fromIndex = command.indexOf(EVENT_START_DATE_MARKER);
        if (fromIndex < EVENT_COMMAND_PREFIX.length()) {
            throw new BogosException(EVENT_FROM_ERROR);
        }

        int toIndex = command.indexOf(EVENT_END_DATE_MARKER);
        if (toIndex < fromIndex) {
            throw new BogosException(EVENT_TO_ERROR);
        }

        String description = getRequiredEventDescription(
                command.substring(EVENT_COMMAND_PREFIX.length(), fromIndex));
        String starting = command.substring(fromIndex + EVENT_START_DATE_MARKER.length(), toIndex).trim();
        if (starting.isBlank()) {
            throw new BogosException(EVENT_FROM_ERROR);
        }
        String ending = command.substring(toIndex + EVENT_END_DATE_MARKER.length()).trim();
        if (ending.isBlank()) {
            throw new BogosException(EVENT_TO_ERROR);
        }
        try {
            return new Event(description, parseDate(starting), parseDate(ending), tags);
        } catch (IllegalArgumentException e) {
            throw new BogosException("Bro be breathing backwards??");
        }
    }

    /**
     * Returns a non-blank to-do description or its command-specific usage error.
     *
     * @param descriptionText Text to validate and trim.
     * @return Trimmed non-blank to-do description.
     * @throws BogosException If the description is blank.
     */
    private String getRequiredTodoDescription(String descriptionText) throws BogosException {
        String trimmedDescription = descriptionText.trim();
        if (trimmedDescription.isBlank()) {
            throw new BogosException(TODO_BODY_ERROR);
        }
        return trimmedDescription;
    }

    /**
     * Returns a non-blank deadline description or its command-specific usage error.
     *
     * @param descriptionText Text to validate and trim.
     * @return Trimmed non-blank deadline description.
     * @throws BogosException If the description is blank.
     */
    private String getRequiredDeadlineDescription(String descriptionText) throws BogosException {
        String trimmedDescription = descriptionText.trim();
        if (trimmedDescription.isBlank()) {
            throw new BogosException(DEADLINE_BODY_ERROR);
        }
        return trimmedDescription;
    }

    /**
     * Returns a non-blank event description or its command-specific usage error.
     *
     * @param descriptionText Text to validate and trim.
     * @return Trimmed non-blank event description.
     * @throws BogosException If the description is blank.
     */
    private String getRequiredEventDescription(String descriptionText) throws BogosException {
        String trimmedDescription = descriptionText.trim();
        if (trimmedDescription.isBlank()) {
            throw new BogosException(EVENT_BODY_ERROR);
        }
        return trimmedDescription;
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
            throw new BogosException("Bogus. Bring Bogos bona-fide YYYY-MM-DD. :[");
        }
    }

    /**
     * Counts occurrences of a parameter token in a command.
     *
     * @param command Command containing zero or more parameter tokens.
     * @param parameter Parameter token to count.
     * @return Number of occurrences of the parameter token.
     */
    private int countParameterOccurrences(String command, String parameter) {
        int occurrenceCount = 0;
        String[] tokens = command.split("\\s+");

        for (String token : tokens) {
            if (token.equals(parameter)) {
                occurrenceCount++;
            }
        }
        return occurrenceCount;
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
                throw new BogosException("Bogus blank #badge. :[");
            }
            if (!uniqueTags.add(tag)) {
                throw new BogosException("Bummer, buplicate #badge. :[");
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
