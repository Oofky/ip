package bogos;

import java.nio.file.Paths;
import java.util.List;

/**
 * Starts the Bogos task-list application and processes user commands.
 */
public class Bogos {
    private static final Ui userInterface = new Ui();
    private static final Parser parser = new Parser();
    private static final Storage storage = new Storage(Paths.get("data", "bogos.txt"));
    private static final TaskList tasks = new TaskList(storage.loadTasks(userInterface));

    /**
     * Runs the application command loop.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        userInterface.showWelcome();

        while (userInterface.hasNextCommand()) {
            String command = userInterface.readCommand();
            boolean hasTasksChanged = false;
            userInterface.showDivider();

            if (command.equals("bye")) {
                userInterface.showGoodbye();
                userInterface.showDivider();
                break;
            }

            try {
                if (command.contains("|")) {
                    throw new BogosException("Bah! Bpipes ('|') banned!");

                } else if (command.equals("list")) {
                    if (!tasks.isEmpty()) {
                        userInterface.showMessage("Behold bulleted board:");
                        for (int i = 1; i <= tasks.size(); i++) {
                            userInterface.showMessage(i + "." + tasks.getTask(i));
                        }
                    } else {
                        throw new BogosException("But board be blank...");
                    }

                } else if (command.startsWith("find ")) {
                    handleFindCommand(command);

                } else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                    handleMarkCommand(command);
                    hasTasksChanged = true;

                } else if (command.startsWith("delete ")) {
                    handleDeleteCommand(command);
                    hasTasksChanged = true;

                } else if (command.startsWith("todo ")) {
                    addTask(parser.parseTask(command));
                    hasTasksChanged = true;

                } else if (command.startsWith("deadline ")) {
                    addTask(parser.parseTask(command));
                    hasTasksChanged = true;

                } else if (command.startsWith("event ")) {
                    addTask(parser.parseTask(command));
                    hasTasksChanged = true;

                } else {
                    throw new BogosException("bwhat");
                }

                if (hasTasksChanged) {
                    storage.saveTasks(tasks.asList(), userInterface);
                }

            } catch (BogosException e) {
                userInterface.showMessage(e.getMessage());
            } finally {
                userInterface.showDivider();
            }
        }
    }

    /**
     * Marks or unmarks the task specified by a mark-related command and displays the result.
     *
     * @param command Mark or unmark command to process.
     * @throws BogosException If the task number is invalid or its status is unchanged.
     */
    private static void handleMarkCommand(String command) throws BogosException {
        boolean isMarkCommand = command.startsWith("mark");
        String taskNumberText = command.substring(isMarkCommand ? "mark ".length() : "unmark ".length()).trim();
        Task task = tasks.getTask(parser.parseTaskNumber(taskNumberText));

        if (task.isDone() == isMarkCommand) {
            throw new BogosException("Bro, box basically behaved beforehand.");
        }

        if (isMarkCommand) {
            task.markAsDone();
            userInterface.showMessage("Bravo! Bogos boxed bullet:");
        } else {
            task.markAsNotDone();
            userInterface.showMessage("Bet! Bogos blanked box:");
        }
        userInterface.showMessage("  " + task);
    }

    /**
     * Deletes the task specified by a delete command and displays the result.
     *
     * @param command Delete command to process.
     * @throws BogosException If the task number is invalid.
     */
    private static void handleDeleteCommand(String command) throws BogosException {
        String taskNumberText = command.substring("delete ".length()).trim();
        Task task = tasks.removeTask(parser.parseTaskNumber(taskNumberText));
        userInterface.showMessage("Brilliant! Bye bye bullet:");
        userInterface.showMessage("  " + task);
        userInterface.showMessage(Integer.toString(tasks.size()) + " bullet(s) being.");
    }

    /**
     * Finds tasks with descriptions containing the keyword in a find command.
     *
     * @param command Find command to process.
     * @throws BogosException If the keyword is empty.
     */
    private static void handleFindCommand(String command) throws BogosException {
        String keyword = command.substring("find ".length()).trim();
        if (keyword.isEmpty()) {
            throw new BogosException("bwhat keyword");
        }

        List<Task> matchingTasks = tasks.findTasks(keyword);
        userInterface.showMessage("Bogos brings befitting bullets:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            userInterface.showMessage((i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Adds a task to the list and displays the newly added task.
     *
     * @param newTask Task to add.
     */
    private static void addTask(Task newTask) {
        tasks.addTask(newTask);
        userInterface.showMessage("Boom! Bullet born: ");
        userInterface.showMessage("  " + newTask);
        userInterface.showMessage(Integer.toString(tasks.size()) + " bullet(s) being.");
    }

}
