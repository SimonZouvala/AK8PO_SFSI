package fai.utb.db.exception;

/**
 * This exception is thrown when validation of entity fails.
 *
 * @author Šimon Zouvala
 */
public class ValidationException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>ValidationException</code> without detail message.
     */
    public ValidationException() {
    }

    /**
     * Constructs an instance of
     * <code>ValidationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ValidationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of
     * <code>ValidationException</code> with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the detail of cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
