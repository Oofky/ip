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
Hey. I'm Bogos.
Can I help you get your photos printed? (How can I help you?)""";
    private static final String BYE_STRING = "Bogos binted. (Bye!)";
    private static final int MAX_TASKS = 100;
    private static final String[] tasks = new String[MAX_TASKS];

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
                for (int i = 0; i < numberOfTasks; i++) {
                    System.out.println(INDENT_STRING + (i + 1) + ". " + tasks[i]);
                }
            } else if (numberOfTasks < MAX_TASKS) { // Defensive check
                tasks[numberOfTasks] = command;
                numberOfTasks++;
                System.out.println(INDENT_STRING + "added: " + command);
            }

            System.out.println(HORIZ_STRING);
        }
    }
}
