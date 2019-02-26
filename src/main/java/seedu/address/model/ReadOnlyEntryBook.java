package seedu.address.model;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import seedu.address.model.entry.Entry;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyEntryBook extends Observable {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Entry> getPersonList();

}
