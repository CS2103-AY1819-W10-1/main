package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.UniqueEntryList;

/**
 * Wraps all data at the entry book level
 * Duplicates are not allowed (by .isSameEntry comparison)
 */
public class EntryBook implements ReadOnlyEntryBook {

    private final UniqueEntryList entries;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        entries = new UniqueEntryList();
    }

    public EntryBook() {}

    /**
     * Creates an EntryBook using the Entries in the {@code toBeCopied}
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
    public void setEntries(List<Entry> entries) {
        this.entries.setEntries(entries);
        indicateModified();
    }

    /**
     * Resets the existing data of this {@code EntryBook} with {@code newData}.
     */
    public void resetData(ReadOnlyEntryBook newData) {
        requireNonNull(newData);

        setEntries(newData.getEntryList());
    }

    //// entry-level operations

    /**
     * Returns true if a entry with the same identity as {@code entry} exists in the entry book.
     */
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return entries.contains(entry);
    }

    /**
     * Adds a entry to the entry book.
     * The entry must not already exist in the entry book.
     */
    public void addEntry(Entry p) {
        entries.add(p);
        indicateModified();
    }

    /**
     * Replaces the given entry {@code target} in the list with {@code editedEntry}.
     * {@code target} must exist in the entry book.
     * The entry identity of {@code editedEntry} must not be the same as another existing entry in the entry book.
     */
    public void setEntry(Entry target, Entry editedEntry) {
        requireNonNull(editedEntry);

        entries.setEntry(target, editedEntry);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code EntryBook}.
     * {@code key} must exist in the entry book.
     */
    public void removeEntry(Entry key) {
        entries.remove(key);
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
     * Notifies listeners that the entry book has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return entries.asUnmodifiableObservableList().size() + " entries";
        // TODO: refine later
    }

    @Override
    public ObservableList<Entry> getEntryList() {
        return entries.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EntryBook // instanceof handles nulls
                && entries.equals(((EntryBook) other).entries));
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }
}
