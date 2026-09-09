package bogos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for finding tasks in a task list.
 */
public class TaskListTest {
    /**
     * Verifies that finding tasks matches description keywords without regard to case.
     */
    @Test
    public void findTasks_matchingKeyword_returnsMatchesInOriginalOrder() {
        Todo firstMatchingTask = new Todo("read book");
        Deadline secondMatchingTask = new Deadline("return BOOK", LocalDate.of(2026, 9, 6));
        TaskList tasks = new TaskList(List.of(firstMatchingTask, new Todo("buy milk"), secondMatchingTask));

        List<Task> matchingTasks = tasks.findTasks("Book");

        assertEquals(List.of(firstMatchingTask, secondMatchingTask), matchingTasks);
    }

    /**
     * Verifies that finding tasks returns no tasks when no description matches.
     */
    @Test
    public void findTasks_noMatchingKeyword_returnsEmptyList() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        List<Task> matchingTasks = tasks.findTasks("milk");

        assertEquals(List.of(), matchingTasks);
    }
}
