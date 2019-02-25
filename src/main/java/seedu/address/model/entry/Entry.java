package seedu.address.model.entry;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents an Entry in the entry book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Entry {

    // Identity fields
    private final Link link;

    // Data fields
    private final Title title;
    private final Comment comment;
    // private final Description description;
    // private final SiteName siteName;

    // private final Set<Feed> feeds = new HashSet<>();
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Entry (Link link,
                  Title title,
                  Comment comment,
                  // Description description,
                  // SiteName siteName,
                  // Set<Feed> feeds,
                  Set<Tag> tags) {
        requireAllNonNull(link, title, tags);
        this.link = link;
        this.title = title;
        this.comment = comment;
        // this.description = description;
        // this.siteName = siteName;
        // this.feeds = feeds;
        this.tags.addAll(tags);
    }

    public Link getLink () {
        return link;
    }

    public Title getTitle() {
        return title;
    }

    public Comment getComment() {
        return comment;
    }

    /*
    public Description getDescription() {
        return description;
    }

    public SiteName getSiteName() {
        return siteName;
    }
    */

    /**
     * Returns an immutable feed set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     public Set<Feed> getFeed() {
     return Collections.unmodifiableSet(feeds);
     }
     */

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both entries have the same link.
     * This defines a weaker notion of equality between two entries.
     */
    public boolean isSameEntry(Entry otherEntry) {
        if (otherEntry == this) {
            return true;
        }

        return otherEntry != null
                && otherEntry.getLink().equals(getLink());
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
        return otherEntry.getLink().equals(getLink())
                && otherEntry.getTitle().equals(getTitle())
                && otherEntry.getComment().equals(getComment())
                // && otherEntry.getSiteName().equals(getSiteName())
                // && otherEntry.getDescription().equals(getDescription())
                // && otherEntry.getFeeds().equals(getFeeds())
                && otherEntry.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(link,
                            title,
                            comment,
                            // description,
                            // siteName,
                            // feeds,
                            tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle())
                .append(" Link: ")
                .append(getLink())
                .append(" Comment: ")
                .append(getComment());
                // .append(" SitName: ")
                // .append(getSitName())
                // .append(" Description: ")
                // .append(getDescription())
        builder.append(" Tags: ");
        getTags().forEach(builder::append);
        // builder.append(" Feeds: ");
        // getFeeds().forEach(builder::append);
        return builder.toString();
    }

}
