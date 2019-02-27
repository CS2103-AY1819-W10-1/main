package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.entry.Entry;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Entry.
 */
public class EntryUtil {

    /**
     * Returns an add command string for adding the {@code entry}.
     */
    public static String getAddCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(entry);
    }

    /**
     * Returns an add command string using alias for adding the {@code entry}.
     */
    public static String getAddAliasCommand(Entry entry) {
        return AddCommand.COMMAND_ALIAS + " " + getPersonDetails(entry);
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details.
     */
    public static String getPersonDetails(Entry entry) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_TITLE + entry.getTitle().fullTitle + " ");
        sb.append(PREFIX_COMMENT + entry.getComment().value + " ");
        sb.append(PREFIX_LINK + entry.getLink().value + " ");
        sb.append(PREFIX_ADDRESS + entry.getAddress().value + " ");
        entry.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getTitle().ifPresent(name -> sb.append(PREFIX_TITLE).append(name.fullTitle).append(" "));
        descriptor.getComment().ifPresent(phone -> sb.append(PREFIX_COMMENT).append(phone.value).append(" "));
        descriptor.getLink().ifPresent(email -> sb.append(PREFIX_LINK).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
