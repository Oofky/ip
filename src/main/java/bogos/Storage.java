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
    private static final int TYPE_INDEX = 0;
    private static final int STATUS_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    private static final int TODO_FIELD_COUNT = 3;
    private static final int DEADLINE_FIELD_COUNT = 4;
    private static final int EVENT_FIELD_COUNT = 5;

    private final File dataFile;

    /**
     * Creates storage backed by the given path.
     *
     * @param filePath Path of the data file.
     */
    public Storage(Path filePath) {
        dataFile = filePath.toFile();
    }

    /**
     * Loads valid tasks from the data file and reports recoverable file errors through the UI.
     *
     * @param ui UI used to report skipped or unreadable task data.
     * @return Valid tasks found in the data file.
     */
    public List<Task> loadTasks(Ui userInterface) {
        List<Task> tasks = new ArrayList<>();
        if (!dataFile.exists()) {
            return tasks;
        }

        try (Scanner fileScanner = new Scanner(dataFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Task task = loadTask(line, userInterface);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            userInterface.showMessage("Bad boot: " + e.getMessage() + ", backup bypassed");
        }
        return tasks;
    }

    /**
     * Loads one stored task record and reports malformed records through the UI.
     *
     * @param line Stored task record.
     * @param userInterface UI used to report malformed task data.
     * @return Task represented by the record, or {@code null} when the record is malformed.
     */
    private Task loadTask(String line, Ui userInterface) {
        String[] parts = line.split(" \\| ", -1);

        try {
            Task task = createTask(parts);
            if (Boolean.parseBoolean(parts[STATUS_INDEX])) {
                task.markAsDone();
            }
            return task;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            userInterface.showMessage("Bad backup: " + line + ", bypassed");
            return null;
        }
    }

    /**
     * Saves every task to the data file, reporting an I/O failure through the UI.
     *
     * @param tasks Tasks to save.
     * @param ui UI used to report an I/O failure.
     */
    public void saveTasks(List<Task> tasks, Ui userInterface) {
        if (dataFile.getParentFile() != null) {
            dataFile.getParentFile().mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(dataFile)) {
            for (Task task : tasks) {
                fileWriter.write(task.toFileFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            userInterface.showMessage("Bad boot: " + e.getMessage() + ", backup bypassed");
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
        if (parts.length < TODO_FIELD_COUNT) {
            throw new IllegalArgumentException("Too few fields for task.");
        }
        if (!parts[STATUS_INDEX].equals("true") && !parts[STATUS_INDEX].equals("false")) {
            throw new IllegalArgumentException("Invalid isDone status.");
        }

        TaskType taskType = TaskType.fromStorageCode(parts[TYPE_INDEX]);
        return switch (taskType) {
        case TODO -> {
            verifyFieldCount(parts, TODO_FIELD_COUNT);
            yield new Todo(parts[DESCRIPTION_INDEX]);
        }
        case DEADLINE -> {
            verifyFieldCount(parts, DEADLINE_FIELD_COUNT);
            yield new Deadline(parts[DESCRIPTION_INDEX], LocalDate.parse(parts[DATE_INDEX]));
        }
        case EVENT -> {
            verifyFieldCount(parts, EVENT_FIELD_COUNT);
            yield new Event(parts[DESCRIPTION_INDEX], LocalDate.parse(parts[DATE_INDEX]),
                    LocalDate.parse(parts[END_DATE_INDEX]));
        }
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
