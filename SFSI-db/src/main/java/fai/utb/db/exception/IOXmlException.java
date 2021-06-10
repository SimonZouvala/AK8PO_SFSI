package fai.utb.db.exception;

/**
 * This exception is thrown when problem with XML file.
 *
 * @author Šimon Zouvala
 */
public class IOXmlException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>IOXmlException</code> without detail message.
     */
    public IOXmlException() {
        super();
    }

    /**
     * Constructs an instance of
     * <code>IOXmlException</code> with the specified detail message.
     *
     * @param message the detail message.
     */
    public IOXmlException(String message) {
        super(message);
    }

    /**
     * Constructs an instance of
     * <code>IOXmlException</code> with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the detail of cause
     */
    public IOXmlException(String message, Throwable cause) {
        super(message, cause);
    }
}
