package exception;

/**
 * The type ServiceException handles all errors in data entry, missing objects, and general application errors,
 * providing customized messages to the user interface.
 *
 * @version 1.0
 */
public class ServiceException extends RuntimeException {

    /**
     * Instantiates a new ServiceException with a specified message.
     *
     * @param message the detailed error message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Instantiates a new ServiceException with a default error message.
     */
    public ServiceException() {
        super("Error de servicio.");
    }
}
