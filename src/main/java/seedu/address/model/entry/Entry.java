package seedu.address.model.entry;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Entry in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Entry {

    // Identity fields
    private final Title title;
    private final Comment comment;
    private final Link link;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Entry(Title title, Comment comment, Link link, Address address, Set<Tag> tags) {
        requireAllNonNull(title, comment, link, address, tags);
        this.title = title;
        this.comment = comment;
        this.link = link;
        this.address = address;
        this.tags.addAll(tags);
    }

    public Title getTitle() {
        return title;
    }

    public Comment getComment() {
        return comment;
    }

    public Link getLink() {
        return link;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons of the same title have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Entry otherEntry) {
        if (otherEntry == this) {
            return true;
        }

        return otherEntry != null
                && otherEntry.getTitle().equals(getTitle())
                && (otherEntry.getComment().equals(getComment()) || otherEntry.getLink().equals(getLink()));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Entry)) {
            return false;
        }

        Entry otherEntry = (Entry) other;
        return otherEntry.getTitle().equals(getTitle())
                && otherEntry.getComment().equals(getComment())
                && otherEntry.getLink().equals(getLink())
                && otherEntry.getAddress().equals(getAddress())
                && otherEntry.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(title, comment, link, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle())
                .append(" Comment: ")
                .append(getComment())
                .append(" Link: ")
                .append(getLink())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
