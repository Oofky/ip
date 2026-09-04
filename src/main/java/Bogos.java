import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Bogos {
    private static final String HORIZ_STRING = "____________________________________________________________";
    private static final String INDENT_STRING = "         ";
    private static final String BANNER_STRING = """
      ___             __ _                  
     | _ )    ___    / _` |   ___     ___   
     | _ \\   / _ \\   \\__, |  / _ \\   (_-<   
     |___/   \\___/   |___/   \\___/   /__/_  
   _|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| 
   "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'
____________________________________________________________
Blessings! Bogos beckons. Bring Bogos business? :]""";
    private static final String BYE_STRING = "Bye bye! :]";
    private static final String DATA_FILE_PATH = "./data/bogos.txt";
    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(BANNER_STRING);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine();
            System.out.println(HORIZ_STRING);

            if (command.equals("bye")) {
                bogosSay(BYE_STRING);
                System.out.println(HORIZ_STRING);
                break;
            } 
            
            try {
                if (command.equals("list")) {
                    if (tasks.size() > 0) {
                        bogosSay("Behold bulleted board:");
                        for (int i = 0; i < tasks.size(); i++) {
                            bogosSay((i + 1) + "." + tasks.get(i).toString());
                        }
                    } else { throw new BogosException("But board be blank..."); }
                    
                } else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                    boolean mark = command.startsWith("mark");
                    String taskNumberText = command.substring(mark ? "mark ".length() : "unmark ".length()).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            throw new BogosException("Bummer. Bullet beyond bounds. :[");
                        } else {
                            int taskIndex = taskNumber - 1;
                            Task task = tasks.get(taskIndex);

                            if (task.isDone() == mark) { // Redundant action
                                throw new BogosException("Bro, box basically behaved beforehand.");
                            } else if (mark) {
                                task.markAsDone();
                                bogosSay("Bravo! Bogos boxed bullet:");
                                bogosSay("  " + task.toString());
                            } else {
                                task.markAsNotDone();
                                bogosSay("Bet! Bogos blanked box:");
                                bogosSay("  " + task.toString());
                            }
                        }
                    } catch (NumberFormatException exception) {
                        throw new BogosException("Bogus. Bring Bogos base-ten. :[");
                    }

                } else if (command.startsWith("delete ")) {
                    String taskNumberText = command.substring("delete ".length()).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            throw new BogosException("Bummer. Bullet beyond bounds. :[");
                        } else {
                            int taskIndex = taskNumber - 1;
                            Task task = tasks.get(taskIndex);
                            tasks.remove(task);
                            bogosSay("Brilliant! Bye bye bullet:");
                            bogosSay("  " + task.toString());
                            bogosSay(Integer.toString(tasks.size()) + " bullet(s) being.");
                        }
                    } catch (NumberFormatException exception) {
                        throw new BogosException("Bogus. Bring Bogos base-ten. :[");
                    }

                } else if (command.startsWith("todo ")) {
                    String todoText = command.substring("todo ".length()).trim();
                    verifyInputBlank(todoText);
                    addTask(new Todo(todoText));
                    
                } else if (command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    if (byIndex >= "deadline ".length()) { // Check if /by exists and is not empty string
                        String deadlineText = command.substring("deadline ".length(), byIndex).trim();
                        String by = command.substring(byIndex + " /by ".length()).trim();
                        verifyInputBlank(deadlineText, by);
                        addTask(new Deadline(deadlineText, by));
                    } else { throw new BogosException("bwhat [deadline ... /by ...]"); }

                } else if (command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    if (fromIndex >= "event ".length() && toIndex >= fromIndex) { // Check if /from and /to exists and are not empty strings
                        String eventText = command.substring("event ".length(), fromIndex).trim();
                        String starting = command.substring(fromIndex + " /from ".length(), toIndex).trim();
                        String ending = command.substring(toIndex + " /to ".length()).trim();
                        verifyInputBlank(eventText, starting, ending);
                        addTask(new Event(eventText, starting, ending));
                    } else { throw new BogosException("bwhat [event ... /from ... /to ...]"); }

                } else {
                    throw new BogosException("bwhat");
                }

                saveTasks();

            } catch (BogosException e) {
                bogosSay(e.getMessage());
            } finally {
                System.out.println(HORIZ_STRING);
            }
        }
    }

    private static void bogosSay(String message) {
        System.out.println(INDENT_STRING + message);
    }

    private static void addTask(Task newTask) {
        tasks.add(newTask);
        bogosSay("Boom! Bullet born: ");
        bogosSay("  " + newTask.toString());
        bogosSay(Integer.toString(tasks.size()) + " bullet(s) being.");
    }

    private static void verifyInputBlank(String... inputs) throws BogosException {
        for (String s : inputs) {
            if (s.isBlank()) { throw new BogosException("bwhat body"); }
        }
    }

    private static void saveTasks() {
        try {
            File file = new File(DATA_FILE_PATH);
            // Create the parent directories (e.g., ./data) if they don't exist
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            
            FileWriter fw = new FileWriter(file);
            for (Task task : tasks) {
                fw.write(task.toString() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            bogosSay("Bad browse: " + e.getMessage());
        }
    }
}
