package exception;


public class RevHireException extends Exception {
    public RevHireException(String message) {
        super(message);
    }
    public RevHireException(String message, Throwable cause) {
        super(message, cause);
    }
}
