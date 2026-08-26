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
    private static final int MAX_TASKS = 100;
    private static final Task[] tasks = new Task[MAX_TASKS];

    public static void main(String[] args) {
        System.out.println(BANNER_STRING);

        Scanner scanner = new Scanner(System.in);
        int numberOfTasks = 0;

        while (true) {
            String command = scanner.nextLine();
            System.out.println(HORIZ_STRING);

            if (command.equals("bye")) {
                System.out.println(INDENT_STRING + BYE_STRING);
                System.out.println(HORIZ_STRING);
                break;
            } else if (command.equals("list")) {
                System.out.println(INDENT_STRING + "Behold bullets:");
                for (int i = 0; i < numberOfTasks; i++) {
                    System.out.println(INDENT_STRING + (i + 1) + "." + tasks[i].toString());
                }
            } else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                boolean mark = command.startsWith("mark");
                String taskNumberText = command.substring(mark ? "mark ".length() : "unmark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > numberOfTasks) {
                        System.out.println(INDENT_STRING + "Bummer. Bullet beyond bounds. :[");
                    } else {
                        int taskIndex = taskNumber - 1;
                        Task task = tasks[taskIndex];

                        if (task.isDone() == mark) { // Redundant action
                            System.out.println(INDENT_STRING + "Bro, box basically behaved beforehand.");
                        } else if (mark) {
                            task.markAsDone();
                            System.out.println(INDENT_STRING + "Bravo! Bogos boxed bullet:");
                            System.out.println(INDENT_STRING + "  " + task.toString());
                        } else {
                            task.markAsNotDone();
                            System.out.println(INDENT_STRING + "Bet! Bogos blanked box:");
                            System.out.println(INDENT_STRING + "  " + task.toString());
                        }
                    }
                } catch (NumberFormatException exception) {
                    System.out.println(INDENT_STRING + "Bogus. Bring Bogos base-ten. :[");
                }
            } else if (command.startsWith("todo ")) {
                String todoText = command.substring("todo ".length()).trim();
                Task newTask = new Todo(todoText);
                tasks[numberOfTasks] = newTask;
                numberOfTasks++;
                System.out.println(INDENT_STRING + "Boom! Bullet born: ");
                System.out.println(INDENT_STRING + "  " + newTask.toString());
                System.out.println(INDENT_STRING + numberOfTasks + " bullet(s) being.");
            } else if (command.startsWith("deadline ")) {
                int byIndex = command.indexOf(" /by ");
                if (byIndex > "deadline".length()) { // Check if /by exists AND if there's a task description
                    String deadlineText = command.substring("deadline ".length(), byIndex).trim();
                    String by = command.substring(byIndex + " /by ".length()).trim();
                    Task newTask = new Deadline(deadlineText, by);
                    tasks[numberOfTasks] = newTask;
                    numberOfTasks++;
                    System.out.println(INDENT_STRING + "Boom! Bullet born: ");
                    System.out.println(INDENT_STRING + "  " + newTask.toString());
                    System.out.println(INDENT_STRING + numberOfTasks + " bullet(s) being.");
                } else {
                    System.out.println(INDENT_STRING + "bwhat");
                }
            } else if (command.startsWith("event ")) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ");
                if (fromIndex > "event".length() && toIndex > fromIndex) {
                    String eventText = command.substring("event ".length(), fromIndex).trim();
                    String starting = command.substring(fromIndex + " /from ".length(), toIndex).trim();
                    String ending = command.substring(toIndex + " /to ".length()).trim();
                    Task newTask = new Event(eventText, starting, ending);
                    tasks[numberOfTasks] = newTask;
                    numberOfTasks++;
                    System.out.println(INDENT_STRING + "Boom! Bullet born: ");
                    System.out.println(INDENT_STRING + "  " + newTask.toString());
                    System.out.println(INDENT_STRING + numberOfTasks + " bullet(s) being.");
                } else {
                    System.out.println(INDENT_STRING + "bwhat");
                }
            } else {
                System.out.println(INDENT_STRING + "bwhat");
            }

            System.out.println(HORIZ_STRING);
        }
    }
}
