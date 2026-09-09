package bogos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests for converting user commands into tasks and task numbers.
 */
public class ParserTest {
    /**
     * Verifies that a valid to-do command creates an incomplete to-do.
     *
     * @throws BogosException If the valid command cannot be parsed.
     */
    @Test
    public void parseTask_todo_success() throws BogosException {
        Parser parser = new Parser();

        Task task = parser.parseTask("todo CS3241 assignment");

        Todo todo = assertInstanceOf(Todo.class, task);
        assertEquals("CS3241 assignment", todo.getDescription());
        assertFalse(todo.isDone());
    }

    /**
     * Verifies that a valid deadline command creates an incomplete deadline.
     *
     * @throws BogosException If the valid command cannot be parsed.
     */
    @Test
    public void parseTask_deadline_success() throws BogosException {
        Parser parser = new Parser();

        Task task = parser.parseTask("deadline submit report /by 2026-09-15");

        Deadline deadline = assertInstanceOf(Deadline.class, task);
        assertEquals("submit report", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 15), deadline.getDueDate());
        assertFalse(deadline.isDone());
    }

    /**
     * Verifies that a valid event command creates an incomplete event.
     *
     * @throws BogosException If the valid command cannot be parsed.
     */
    @Test
    public void parseTask_event_success() throws BogosException {
        Parser parser = new Parser();

        Task task = parser.parseTask("event project meeting /from 2026-09-15 /to 2026-09-16");

        Event event = assertInstanceOf(Event.class, task);
        assertEquals("project meeting", event.getDescription());
        assertEquals(LocalDate.of(2026, 9, 15), event.getStartDate());
        assertEquals(LocalDate.of(2026, 9, 16), event.getEndDate());
        assertFalse(event.isDone());
    }

    /**
     * Verifies that an unrecognised command produces a parser error.
     */
    @Test
    public void parseTask_nonsense_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("todooles homework"));

        assertEquals("bwhat", exception.getMessage());
    }

    /**
     * Verifies that a to-do command requires a description.
     */
    @Test
    public void parseTask_todoWithEmptyDesc_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("todo "));

        assertEquals("bwhat body", exception.getMessage());
    }

    /**
     * Verifies that surrounding whitespace is removed from a to-do description.
     *
     * @throws BogosException If the valid command cannot be parsed.
     */
    @Test
    public void parseTask_todoWithLeadingTrailingWhitespace_trimmedDescription() throws BogosException {
        Parser parser = new Parser();

        Todo todo = assertInstanceOf(Todo.class, parser.parseTask("todo   revise  notes   "));

        assertEquals("revise  notes", todo.getDescription());
    }

    /**
     * Verifies that a deadline command requires its due-date marker.
     */
    @Test
    public void parseTask_deadlineWithNoBy_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("deadline submit report"));

        assertEquals("bwhat [deadline ... /by ...]", exception.getMessage());
    }

    /**
     * Verifies that a deadline command requires a due date.
     */
    @Test
    public void parseTask_deadlineWithEmptyBy_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("deadline submit report /by "));

        assertEquals("bwhat body", exception.getMessage());
    }

    /**
     * Verifies that a deadline command requires a description.
     */
    @Test
    public void parseTask_deadlineWithEmptyDesc_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("deadline  /by 2026-09-15"));

        assertEquals("bwhat body", exception.getMessage());
    }

    /**
     * Verifies that a deadline date must use the ISO-8601 format.
     */
    @Test
    public void parseTask_deadlineWithInvalidByDate_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("deadline submit report /by tomorrow"));

        assertEquals("bwhat [yyyy-mm-dd]", exception.getMessage());
    }

    /**
     * Verifies that an event command requires its start-date marker.
     */
    @Test
    public void parseTask_eventWithNoFrom_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("event project meeting /to 2026-09-16"));

        assertEquals("bwhat [event ... /from ... /to ...]", exception.getMessage());
    }

    /**
     * Verifies that an event command requires a start date.
     */
    @Test
    public void parseTask_eventWithEmptyFrom_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("event project meeting /from  /to 2026-09-16"));

        assertEquals("bwhat body", exception.getMessage());
    }

    /**
     * Verifies that an event command requires its end-date marker.
     */
    @Test
    public void parseTask_eventWithNoTo_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("event project meeting /from 2026-09-15"));

        assertEquals("bwhat [event ... /from ... /to ...]", exception.getMessage());
    }

    /**
     * Verifies that an event command requires an end date.
     */
    @Test
    public void parseTask_eventWithEmptyTo_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("event project meeting /from 2026-09-15 /to "));

        assertEquals("bwhat body", exception.getMessage());
    }

    /**
     * Verifies that an event cannot end before it starts.
     */
    @Test
    public void parseTask_eventWithReverseDates_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("event project meeting /from 2026-09-16 /to 2026-09-15"));

        assertEquals("Bro be breathing backwards??", exception.getMessage());
    }

    /**
     * Verifies that an event start date must use the ISO-8601 format.
     */
    @Test
    public void parseTask_eventWithInvalidFromDate_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTask("event project meeting /from tomorrow /to 2026-09-16"));

        assertEquals("bwhat [yyyy-mm-dd]", exception.getMessage());
    }

    /**
     * Verifies that a whole-number task position is parsed successfully.
     *
     * @throws BogosException If the valid task number cannot be parsed.
     */
    @Test
    public void parseTaskNumber_integer_success() throws BogosException {
        Parser parser = new Parser();

        int taskNumber = parser.parseTaskNumber("42");

        assertEquals(42, taskNumber);
    }

    /**
     * Verifies that a decimal task position produces a parser error.
     */
    @Test
    public void parseTaskNumber_double_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTaskNumber("1.5"));

        assertEquals("Bogus. Bring Bogos base-ten. :[", exception.getMessage());
    }

    /**
     * Verifies that a non-numeric task position produces a parser error.
     */
    @Test
    public void parseTaskNumber_alphabet_exceptionThrown() {
        Parser parser = new Parser();

        BogosException exception = assertThrows(BogosException.class,
                () -> parser.parseTaskNumber("one"));

        assertEquals("Bogus. Bring Bogos base-ten. :[", exception.getMessage());
    }
}
