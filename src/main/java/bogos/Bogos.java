package bogos;

import java.nio.file.Paths;

/**
 * Starts the Bogos task-list application and processes user commands.
 */
public class Bogos {
    private static final Ui ui = new Ui();
    private static final Parser parser = new Parser();
    private static final Storage storage = new Storage(Paths.get("data", "bogos.txt"));
    private static final TaskList tasks = new TaskList(storage.loadTasks(ui));

    /**
     * Runs the application command loop.
     *
     * @param args Command-line arguments, which are not used.
     */
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
                    addTask(parser.parseTask(command));
                    tasksHaveChanged = true;
                    
                } else if (command.startsWith("deadline ")) {
                    addTask(parser.parseTask(command));
                    tasksHaveChanged = true;

                } else if (command.startsWith("event ")) {
                    addTask(parser.parseTask(command));
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

    /**
     * Marks or unmarks the task specified by a mark-related command and displays the result.
     *
     * @param command Mark or unmark command to process.
     * @throws BogosException If the task number is invalid or its status is unchanged.
     */
    private static void handleMarkCommand(String command) throws BogosException {
        boolean mark = command.startsWith("mark");
        Task task = tasks.getTask(parser.parseTaskNumber(command.substring(mark ? "mark ".length() : "unmark ".length()).trim()));

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

    /**
     * Deletes the task specified by a delete command and displays the result.
     *
     * @param command Delete command to process.
     * @throws BogosException If the task number is invalid.
     */
    private static void handleDeleteCommand(String command) throws BogosException {
        Task task = tasks.removeTask(parser.parseTaskNumber(command.substring("delete ".length()).trim()));
        ui.showMessage("Brilliant! Bye bye bullet:");
        ui.showMessage("  " + task);
        ui.showMessage(Integer.toString(tasks.size()) + " bullet(s) being.");
    }

    /**
     * Adds a task to the list and displays the newly added task.
     *
     * @param newTask Task to add.
     */
    private static void addTask(Task newTask) {
        tasks.addTask(newTask);
        ui.showMessage("Boom! Bullet born: ");
        ui.showMessage("  " + newTask);
        ui.showMessage(Integer.toString(tasks.size()) + " bullet(s) being.");
    }

}
