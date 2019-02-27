package seedu.address.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Entry's link in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidLink(String)}
 */
public class Link {

    public static final String MESSAGE_CONSTRAINTS = "Links should be of the format protocol://domain/filename.";

    /*
    private static final String SPECIAL_CHARACTERS = "!#$%&'*+/=?`{|}~^.-";
    // alphanumeric and special characters
    private static final String LOCAL_PART_REGEX = "^[\\w" + SPECIAL_CHARACTERS + "]+";
    private static final String DOMAIN_FIRST_CHARACTER_REGEX = "[^\\W_]"; // alphanumeric characters except underscore
    private static final String DOMAIN_MIDDLE_REGEX = "[a-zA-Z0-9.-]*"; // alphanumeric, period and hyphen
    private static final String DOMAIN_LAST_CHARACTER_REGEX = "[^\\W_]$";
    public static final String VALIDATION_REGEX = LOCAL_PART_REGEX + "@"
            + DOMAIN_FIRST_CHARACTER_REGEX + DOMAIN_MIDDLE_REGEX + DOMAIN_LAST_CHARACTER_REGEX;
            */
    private static final String PATH = "[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String VALIDATION_REGEX = "^(https?|ftp|file)://" + PATH;

    public final String value;

    /**
     * Constructs an {@code Link}.
     *
     * @param link A valid link address.
     */
    public Link(String link) {
        requireNonNull(link);
        checkArgument(isValidLink(link), MESSAGE_CONSTRAINTS);
        value = link;
    }

    /**
     * Returns if a given string is a valid link.
     */
    public static boolean isValidLink(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Link // instanceof handles nulls
                && value.equals(((Link) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
