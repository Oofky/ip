import java.util.Scanner;

/** Handles all console input and output for the Bogos application. */
public class Ui {
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

    /** Displays a console message. */
    public void show(String message) {
        System.out.println(message);
    }
}
