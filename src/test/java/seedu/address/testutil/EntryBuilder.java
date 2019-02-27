package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.entry.Address;
import seedu.address.model.entry.Comment;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Entry objects.
 */
public class EntryBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "https://alice.gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Title title;
    private Comment comment;
    private Link link;
    private Address address;
    private Set<Tag> tags;

    public EntryBuilder() {
        title = new Title(DEFAULT_NAME);
        comment = new Comment(DEFAULT_PHONE);
        link = new Link(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the EntryBuilder with the data of {@code entryToCopy}.
     */
    public EntryBuilder(Entry entryToCopy) {
        title = entryToCopy.getTitle();
        comment = entryToCopy.getComment();
        link = entryToCopy.getLink();
        address = entryToCopy.getAddress();
        tags = new HashSet<>(entryToCopy.getTags());
    }

    /**
     * Sets the {@code Title} of the {@code Entry} that we are building.
     */
    public EntryBuilder withName(String name) {
        this.title = new Title(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Entry} that we are building.
     */
    public EntryBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Entry} that we are building.
     */
    public EntryBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Comment} of the {@code Entry} that we are building.
     */
    public EntryBuilder withPhone(String phone) {
        this.comment = new Comment(phone);
        return this;
    }

    /**
     * Sets the {@code Link} of the {@code Entry} that we are building.
     */
    public EntryBuilder withEmail(String email) {
        this.link = new Link(email);
        return this;
    }

    public Entry build() {
        return new Entry(title, comment, link, address, tags);
    }

}
