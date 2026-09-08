import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.nio.file.Paths;

public class Bogos {
    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(Paths.get("data", "bogos.txt"));
    private static final TaskList tasks = new TaskList(storage.loadTasks(ui));

    public static void main(String[] args) {
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
                        for (int i = 1; i <= tasks.size(); i++) {
                            ui.showMessage(i + "." + tasks.getTask(i));
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
                    storage.saveTasks(tasks.asList(), ui);
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
        Task task = tasks.getTask(parseTaskNumber(command.substring(mark ? "mark ".length() : "unmark ".length()).trim()));

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
        Task task = tasks.removeTask(parseTaskNumber(command.substring("delete ".length()).trim()));
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
        tasks.addTask(newTask);
        ui.showMessage("Boom! Bullet born: ");
        ui.showMessage("  " + newTask);
        ui.showMessage(Integer.toString(tasks.size()) + " bullet(s) being.");
    }

    /** Parses the task's one-based number from user input. */
    private static int parseTaskNumber(String taskNumberText) throws BogosException {
        try {
            return Integer.parseInt(taskNumberText);
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

}
