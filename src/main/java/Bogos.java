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
    private static final String[] tasks = new String[MAX_TASKS];
    // Records whether the task at the corresponding index in tasks is complete
    private static final boolean[] isDone = new boolean[MAX_TASKS];

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
                    System.out.println(INDENT_STRING + (i + 1) + ".["
                            + (isDone[i] ? "X" : " ") + "] " + tasks[i]);
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
                        
                        if (isDone[taskIndex] == mark) { // Redundant action
                            System.out.println(INDENT_STRING + "Bro, box basically behaved beforehand.");
                        } else if (mark) {
                            System.out.println(INDENT_STRING + "Bravo! Bogos boxed bullet:");
                            System.out.println(INDENT_STRING + "  [X] " + tasks[taskIndex]);
                        } else {
                            System.out.println(INDENT_STRING + "Bet! Bogos blanked box:");
                            System.out.println(INDENT_STRING + "  [ ] " + tasks[taskIndex]);
                        }
                        isDone[taskIndex] = mark;
                    }
                } catch (NumberFormatException exception) {
                    System.out.println(INDENT_STRING + "Bogus. Bring Bogos base-ten. :[");
                }
            } else if (numberOfTasks < MAX_TASKS) { // Defensive check
                tasks[numberOfTasks] = command;
                numberOfTasks++;
                System.out.println(INDENT_STRING + "bullet born: " + command);
            }

            System.out.println(HORIZ_STRING);
        }
    }
}
