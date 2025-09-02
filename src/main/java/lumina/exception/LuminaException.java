package lumina.exception;

/**
 * Custom exception class for handling errors specific to the Lumina application.
 */
public class LuminaException extends Exception {
    /**
     * Constructs a new LuminaException with the specified detail message.
     *
     * @param message the detail message
     */
    public LuminaException(String message) {
        super(message);
    }
}
