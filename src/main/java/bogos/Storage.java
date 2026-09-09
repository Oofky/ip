package bogos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Loads tasks from and saves tasks to the application's data file.
 */
public class Storage {
    private final File file;

    /**
     * Creates storage backed by the given path.
     *
     * @param filePath Path of the data file.
     */
    public Storage(Path filePath) {
        file = filePath.toFile();
    }

    /**
     * Loads valid tasks from the data file and reports recoverable file errors through the UI.
     *
     * @param ui UI used to report skipped or unreadable task data.
     * @return Valid tasks found in the data file.
     */
    public List<Task> loadTasks(Ui ui) {
        List<Task> tasks = new ArrayList<>();
        if (!file.exists()) {
            return tasks;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ", -1);

                try {
                    Task task = createTask(parts);
                    if (Boolean.parseBoolean(parts[1])) {
                        task.markAsDone();
                    }
                    tasks.add(task);
                } catch (DateTimeParseException | IllegalArgumentException e) {
                    ui.showMessage("Bad backup: " + line + ", bypassed");
                }
            }
        } catch (FileNotFoundException e) {
            ui.showMessage("Bad boot: " + e.getMessage() + ", backup bypassed");
        }
        return tasks;
    }

    /**
     * Saves every task to the data file, reporting an I/O failure through the UI.
     *
     * @param tasks Tasks to save.
     * @param ui UI used to report an I/O failure.
     */
    public void saveTasks(List<Task> tasks, Ui ui) {
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Task task : tasks) {
                fileWriter.write(task.toFileFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            ui.showMessage("Bad boot: " + e.getMessage() + ", backup bypassed");
        }
    }

    /**
     * Converts one stored task record into a task after validating its fields.
     *
     * @param parts Fields from a stored task record.
     * @return Task represented by the record.
     * @throws IllegalArgumentException If the record is malformed or has an unknown type.
     */
    private Task createTask(String[] parts) {
        if (parts.length < 3) {
            throw new IllegalArgumentException("Too few fields for task.");
        }
        if (!parts[1].equals("true") && !parts[1].equals("false")) {
            throw new IllegalArgumentException("Invalid isDone status.");
        }

        return switch (parts[0]) {
        case "T" -> {
            verifyFieldCount(parts, 3);
            yield new Todo(parts[2]);
        }
        case "D" -> {
            verifyFieldCount(parts, 4);
            yield new Deadline(parts[2], LocalDate.parse(parts[3]));
        }
        case "E" -> {
            verifyFieldCount(parts, 5);
            yield new Event(parts[2], LocalDate.parse(parts[3]), LocalDate.parse(parts[4]));
        }
        default -> throw new IllegalArgumentException("Unknown task type.");
        };
    }

    /**
     * Verifies that a stored task record has the expected number of fields.
     *
     * @param parts Fields from a stored task record.
     * @param expectedCount Required number of fields.
     * @throws IllegalArgumentException If the record has the wrong number of fields.
     */
    private void verifyFieldCount(String[] parts, int expectedCount) {
        if (parts.length != expectedCount) {
            throw new IllegalArgumentException("Wrong number of fields for task type.");
        }
    }
}
