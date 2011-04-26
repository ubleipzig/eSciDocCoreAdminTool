package de.escidoc.admintool.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -8364575730209547522L;

    /**
     * Constructs a new {@code ResourceNotFoundException} that includes the current stack trace.
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * Constructs a new {@code ResourceNotFoundException} with the current stack trace and the specified detail message.
     * 
     * @param detailMessage
     *            the detail message for this exception.
     */
    public ResourceNotFoundException(final String detailMessage) {
        super(detailMessage);
    }
}
