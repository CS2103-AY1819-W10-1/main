package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.entry.Address;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Title;
import seedu.address.model.entry.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Entry objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Title title;
    private Phone phone;
    private Link link;
    private Address address;
    private Set<Tag> tags;

    public PersonBuilder() {
        title = new Title(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        link = new Link(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code entryToCopy}.
     */
    public PersonBuilder(Entry entryToCopy) {
        title = entryToCopy.getTitle();
        phone = entryToCopy.getPhone();
        link = entryToCopy.getLink();
        address = entryToCopy.getAddress();
        tags = new HashSet<>(entryToCopy.getTags());
    }

    /**
     * Sets the {@code Title} of the {@code Entry} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.title = new Title(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Entry} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Entry} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Entry} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Link} of the {@code Entry} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.link = new Link(email);
        return this;
    }

    public Entry build() {
        return new Entry(title, phone, link, address, tags);
    }

}
