package seedu.address.logic.commands.exceptions;

/**
 * Represents an error which occurs during execution of a {@link Command}.
 */
public class DuplicateEntryCommandException extends CommandException {
    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the entry book";

    public DuplicateEntryCommandException() {
        super(MESSAGE_DUPLICATE_ENTRY);
    }

    public DuplicateEntryCommandException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CommandException} with the specified detail {@code message} and {@code cause}.
     */
    public DuplicateEntryCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
