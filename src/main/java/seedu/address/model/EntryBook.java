package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class EntryBook implements ReadOnlyEntryBook {

    private final UniquePersonList persons;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
    }

    public EntryBook() {}

    /**
     * Creates an EntryBook using the Persons in the {@code toBeCopied}
     */
    public EntryBook(ReadOnlyEntryBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the entry list with {@code entries}.
     * {@code entries} must not contain duplicate entries.
     */
    public void setPersons(List<Entry> entries) {
        this.persons.setPersons(entries);
        indicateModified();
    }

    /**
     * Resets the existing data of this {@code EntryBook} with {@code newData}.
     */
    public void resetData(ReadOnlyEntryBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// entry-level operations

    /**
     * Returns true if a entry with the same identity as {@code entry} exists in the address book.
     */
    public boolean hasPerson(Entry entry) {
        requireNonNull(entry);
        return persons.contains(entry);
    }

    /**
     * Adds a entry to the address book.
     * The entry must not already exist in the address book.
     */
    public void addPerson(Entry p) {
        persons.add(p);
        indicateModified();
    }

    /**
     * Replaces the given entry {@code target} in the list with {@code editedEntry}.
     * {@code target} must exist in the address book.
     * The entry identity of {@code editedEntry} must not be the same as another existing entry in the address book.
     */
    public void setPerson(Entry target, Entry editedEntry) {
        requireNonNull(editedEntry);

        persons.setPerson(target, editedEntry);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code EntryBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Entry key) {
        persons.remove(key);
        indicateModified();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the address book has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Entry> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EntryBook // instanceof handles nulls
                && persons.equals(((EntryBook) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
