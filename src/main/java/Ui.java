import java.util.Scanner;

/** Handles all console input and output for the Bogos application. */
public class Ui {
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final String INDENT = "         ";
    private static final String BANNER = """
      ___             __ _                  
     | _ )    ___    / _` |   ___     ___   
     | _ \\   / _ \\   \\__, |  / _ \\   (_-<   
     |___/   \\___/   |___/   \\___/   /__/_  
   _|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| 
   \"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'
____________________________________________________________
Blessings! Bogos beckons. Bring Bogos business? :]""";
    private static final String GOODBYE = "Bye bye! :]";

    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Returns whether another command is available from the user. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads the next command entered by the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the application greeting. */
    public void showWelcome() {
        System.out.println(BANNER);
    }

    /** Displays the application farewell. */
    public void showGoodbye() {
        showMessage(GOODBYE);
    }

    /** Displays a horizontal separator between application responses. */
    public void showDivider() {
        System.out.println(HORIZONTAL_LINE);
    }

    /** Displays an indented application message. */
    public void showMessage(String message) {
        System.out.println(INDENT + message);
    }
}
