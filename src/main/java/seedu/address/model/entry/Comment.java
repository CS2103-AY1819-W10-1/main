package seedu.address.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Entry's comment in the entry book.
 * Guarantees: immutable; is valid as declared in {@link #isValidComment(String)}
 */
public class Comment {


    public static final String MESSAGE_CONSTRAINTS =
            "Comments can take any values, and it should not be blank";
    public static final String VALIDATION_REGEX = "[^\\s].*";
    public final String value;

    /**
     * Constructs a {@code Comment}.
     *
     * @param comment A valid comment number.
     */
    public Comment(String comment) {
        requireNonNull(comment);
        checkArgument(isValidComment(comment), MESSAGE_CONSTRAINTS);
        value = comment;
    }

    /**
     * Returns true if a given string is a valid comment number.
     */
    public static boolean isValidComment(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Comment // instanceof handles nulls
                && value.equals(((Comment) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
