package seedu.address.testutil;

import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code EntryBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private EntryBook addressBook;

    public AddressBookBuilder() {
        addressBook = new EntryBook();
    }

    public AddressBookBuilder(EntryBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Entry} to the {@code EntryBook} that we are building.
     */
    public AddressBookBuilder withPerson(Entry entry) {
        addressBook.addPerson(entry);
        return this;
    }

    public EntryBook build() {
        return addressBook;
    }
}
