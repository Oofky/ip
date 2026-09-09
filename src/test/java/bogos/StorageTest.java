package bogos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for saving tasks to and loading tasks from a data file.
 */
public class StorageTest {
    /**
     * A JUnit-managed directory that is unique to each test.
     */
    @TempDir
    Path temporaryDirectory;

    /**
     * Verifies that all supported task types are written in storage format.
     *
     * @throws IOException If the test cannot read the temporary data file.
     */
    @Test
    public void saveTasks_withMultipleTaskTypes_writesExpectedFileContents() throws IOException {
        Path file = temporaryDirectory.resolve("bogos.txt");
        Storage storage = new Storage(file);
        Todo todo = new Todo("borrow book");
        todo.markAsDone();
        List<Task> tasks = List.of(
                todo,
                new Deadline("return book", LocalDate.of(2026, 9, 15)),
                new Event("project meeting", LocalDate.of(2026, 9, 15), LocalDate.of(2026, 9, 16)));

        storage.saveTasks(tasks, new Ui());

        assertEquals(List.of(
                "T | true | borrow book",
                "D | false | return book | 2026-09-15",
                "E | false | project meeting | 2026-09-15 | 2026-09-16"), Files.readAllLines(file));
    }

    /**
     * Verifies that valid stored task records are reconstructed correctly.
     *
     * @throws IOException If the test cannot write the temporary data file.
     */
    @Test
    public void loadTasks_withValidTaskRecords_returnsMatchingTasks() throws IOException {
        Path file = temporaryDirectory.resolve("bogos.txt");
        Files.write(file, List.of(
                "T | false | borrow book",
                "D | false | return book | 2026-09-15",
                "E | false | project meeting | 2026-09-15 | 2026-09-16"));
        Storage storage = new Storage(file);

        List<Task> tasks = storage.loadTasks(new Ui());

        assertEquals(3, tasks.size());
        assertEquals("borrow book", tasks.get(0).getDescription());

        Deadline deadline = assertInstanceOf(Deadline.class, tasks.get(1));
        assertEquals("return book", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 15), deadline.getDueDate());

        Event event = assertInstanceOf(Event.class, tasks.get(2));
        assertEquals("project meeting", event.getDescription());
        assertEquals(LocalDate.of(2026, 9, 15), event.getStartDate());
        assertEquals(LocalDate.of(2026, 9, 16), event.getEndDate());
    }

    /**
     * Verifies that a stored completed task remains complete after loading.
     *
     * @throws IOException If the test cannot write the temporary data file.
     */
    @Test
    public void loadTasks_withCompletedTask_marksTaskAsDone() throws IOException {
        Path file = temporaryDirectory.resolve("bogos.txt");
        Files.write(file, List.of("T | true | borrow book"));
        Storage storage = new Storage(file);

        List<Task> tasks = storage.loadTasks(new Ui());

        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0).isDone());
    }

    /**
     * Verifies that loading from a missing data file returns no tasks.
     */
    @Test
    public void loadTasks_whenFileDoesNotExist_returnsEmptyList() {
        Storage storage = new Storage(temporaryDirectory.resolve("missing.txt"));

        List<Task> tasks = storage.loadTasks(new Ui());

        assertTrue(tasks.isEmpty());
    }

    /**
     * Verifies that invalid stored records are skipped while valid ones are loaded.
     *
     * @throws IOException If the test cannot write the temporary data file.
     */
    @Test
    public void loadTasks_withInvalidRecord_skipsInvalidRecord() throws IOException {
        Path file = temporaryDirectory.resolve("bogos.txt");
        Files.write(file, List.of(
                "T | false | borrow book",
                "X | false | unknown task"));
        Storage storage = new Storage(file);

        List<Task> tasks = storage.loadTasks(new Ui());

        assertEquals(1, tasks.size());
        Todo todo = assertInstanceOf(Todo.class, tasks.get(0));
        assertEquals("borrow book", todo.getDescription());
        assertFalse(todo.isDone());
    }
}
