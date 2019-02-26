package seedu.address.testutil;

import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code EntryBook ab = new EntryBookBuilder().withPerson("John", "Doe").build();}
 */
public class EntryBookBuilder {

    private EntryBook addressBook;

    public EntryBookBuilder() {
        addressBook = new EntryBook();
    }

    public EntryBookBuilder(EntryBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Entry} to the {@code EntryBook} that we are building.
     */
    public EntryBookBuilder withPerson(Entry entry) {
        addressBook.addPerson(entry);
        return this;
    }

    public EntryBook build() {
        return addressBook;
    }
}
