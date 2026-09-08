import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bogos {
    private static final String DATA_FILE_PATH = Paths.get(".", "data", "bogos.txt").toString();
    private static final List<Task> tasks = new ArrayList<>();
    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        loadTasks();

        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            boolean tasksHaveChanged = false;
            ui.showDivider();

            if (command.equals("bye")) {
                ui.showGoodbye();
                ui.showDivider();
                break;
            } 
            
            try {
                if (command.contains("|")) {
                    throw new BogosException("Bah! Bpipes ('|') banned!");

                } else if (command.equals("list")) {
                    if (!tasks.isEmpty()) {
                        ui.showMessage("Behold bulleted board:");
                        for (int i = 0; i < tasks.size(); i++) {
                            ui.showMessage((i + 1) + "." + tasks.get(i));
                        }
                    } else {
                        throw new BogosException("But board be blank...");
                    }
                    
                } else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                    handleMarkCommand(command);
                    tasksHaveChanged = true;

                } else if (command.startsWith("delete ")) {
                    handleDeleteCommand(command);
                    tasksHaveChanged = true;

                } else if (command.startsWith("todo ")) {
                    String description = command.substring("todo ".length()).trim();
                    verifyNoInputIsBlank(description);
                    addTask(new Todo(description));
                    tasksHaveChanged = true;
                    
                } else if (command.startsWith("deadline ")) {
                    handleDeadlineCommand(command);
                    tasksHaveChanged = true;

                } else if (command.startsWith("event ")) {
                    handleEventCommand(command);
                    tasksHaveChanged = true;

                } else {
                    throw new BogosException("bwhat");
                }

                if (tasksHaveChanged) {
                    saveTasks();
                }

            } catch (BogosException e) {
                ui.showMessage(e.getMessage());
            } finally {
                ui.showDivider();
            }
        }
    }

    private static void handleMarkCommand(String command) throws BogosException {
        boolean mark = command.startsWith("mark");
        Task task = getTaskByNumberText(command.substring(mark ? "mark ".length() : "unmark ".length()).trim());

        if (task.isDone() == mark) {
            throw new BogosException("Bro, box basically behaved beforehand.");
        }

        if (mark) {
            task.markAsDone();
            ui.showMessage("Bravo! Bogos boxed bullet:");
        } else {
            task.markAsNotDone();
            ui.showMessage("Bet! Bogos blanked box:");
        }
        ui.showMessage("  " + task);
    }

    private static void handleDeleteCommand(String command) throws BogosException {
        Task task = getTaskByNumberText(command.substring("delete ".length()).trim());
        tasks.remove(task);
        ui.showMessage("Brilliant! Bye bye bullet:");
        ui.showMessage("  " + task);
        ui.showMessage(Integer.toString(tasks.size()) + " bullet(s) being.");
    }

    private static void handleDeadlineCommand(String command) throws BogosException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex < "deadline ".length()) { // Check if /by exists and is not empty string
            throw new BogosException("bwhat [deadline ... /by ...]");
        }

        String description = command.substring("deadline ".length(), byIndex).trim();
        String by = command.substring(byIndex + " /by ".length()).trim();
        verifyNoInputIsBlank(description, by);
        addTask(new Deadline(description, parseDate(by)));
    }

    private static void handleEventCommand(String command) throws BogosException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex < "event ".length() || toIndex < fromIndex) { // Check if /from and /to exists and are not empty strings
            throw new BogosException("bwhat [event ... /from ... /to ...]");
        }

        String description = command.substring("event ".length(), fromIndex).trim();
        String starting = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String ending = command.substring(toIndex + " /to ".length()).trim();
        verifyNoInputIsBlank(description, starting, ending);
        try {
            addTask(new Event(description, parseDate(starting), parseDate(ending)));
        } catch (IllegalArgumentException e) {
            throw new BogosException("Bro be breathing backwards??");
        }
    }

    private static void addTask(Task newTask) {
        tasks.add(newTask);
        ui.showMessage("Boom! Bullet born: ");
        ui.showMessage("  " + newTask);
        ui.showMessage(Integer.toString(tasks.size()) + " bullet(s) being.");
    }

    /** Finds the task identified by user input after validating its one-based number. */
    private static Task getTaskByNumberText(String taskNumberText) throws BogosException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new BogosException("Bummer. Bullet beyond bounds. :[");
            }
            return tasks.get(taskNumber - 1);
        } catch (NumberFormatException e) {
            throw new BogosException("Bogus. Bring Bogos base-ten. :[");
        }
    }

    /** Parses an ISO-8601 date and converts parsing failures to a user-facing error. */
    private static LocalDate parseDate(String dateText) throws BogosException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new BogosException("bwhat [yyyy-mm-dd]");
        }
    }

    private static void verifyNoInputIsBlank(String... inputs) throws BogosException {
        for (String s : inputs) {
            if (s.isBlank()) {
                throw new BogosException("bwhat body");
            }
        }
    }

    private static void loadTasks() {
        try {
            File file = new File(DATA_FILE_PATH);
            if (!file.exists()) {
                return; // First time running, no file to load
            }
            
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(" \\| ", -1);
                    Task task;

                    try {
                        if (parts.length < 3) {
                            throw new IllegalArgumentException("Too few fields for task.");
                        }
                        if (!parts[1].equals("true") && !parts[1].equals("false")) {
                            throw new IllegalArgumentException("Invalid isDone status.");
                        }

                        String type = parts[0];
                        boolean isDone = Boolean.parseBoolean(parts[1]);
                        String description = parts[2];

                        switch (type) {
                            case "T":
                                if (parts.length != 3) {
                                    throw new IllegalArgumentException("Wrong number of fields for task type.");
                                }
                                task = new Todo(description);
                                break;
                            case "D":
                                if (parts.length != 4) {
                                    throw new IllegalArgumentException("Wrong number of fields for task type.");
                                }
                                task = new Deadline(description, LocalDate.parse(parts[3]));
                                break;
                            case "E":
                                if (parts.length != 5) {
                                    throw new IllegalArgumentException("Wrong number of fields for task type.");
                                }
                                task = new Event(description, LocalDate.parse(parts[3]), LocalDate.parse(parts[4]));
                                break;
                            default:
                                throw new IllegalArgumentException("Unknown task type.");
                        }

                        if (isDone) {
                            task.markAsDone();
                        }
                        tasks.add(task);

                    } catch (DateTimeParseException | IllegalArgumentException e) {
                        ui.showMessage("Bad backup: " + line + ", bypassed");
                        continue; // Skip
                    }
                }
            }
        } catch (FileNotFoundException e) {
            ui.showMessage("Bad boot: " + e.getMessage() + ", backup bypassed"); // Skip
        }
    }

    private static void saveTasks() {
        try {
            File file = new File(DATA_FILE_PATH);
            // Create the parent directories (e.g., ./data) if they don't exist
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            try (FileWriter fileWriter = new FileWriter(file)) {
                for (Task task : tasks) {
                    fileWriter.write(task.toFileFormat() + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            ui.showMessage("Bad boot: " + e.getMessage() + ", backup bypassed"); // Skip
        }
    }
}
