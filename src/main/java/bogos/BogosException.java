package bogos;

/** Signals an error caused by invalid user input or an unavailable task. */
public class BogosException extends Exception {
    /** Creates an exception with the message to show to the user. */
    public BogosException(String message) {
        super(message);
    }
}
