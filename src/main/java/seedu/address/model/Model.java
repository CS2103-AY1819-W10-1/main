package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.entry.Entry;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Entry> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyEntryBook addressBook);

    /** Returns the EntryBook */
    ReadOnlyEntryBook getAddressBook();

    /**
     * Returns true if a entry with the same identity as {@code entry} exists in the address book.
     */
    boolean hasPerson(Entry entry);

    /**
     * Deletes the given entry.
     * The entry must exist in the address book.
     */
    void deletePerson(Entry target);

    /**
     * Adds the given entry.
     * {@code entry} must not already exist in the address book.
     */
    void addPerson(Entry entry);

    /**
     * Replaces the given entry {@code target} with {@code editedEntry}.
     * {@code target} must exist in the address book.
     * The entry identity of {@code editedEntry} must not be the same as another existing entry in the address book.
     */
    void setPerson(Entry target, Entry editedEntry);

    /** Returns an unmodifiable view of the filtered entry list */
    ObservableList<Entry> getFilteredPersonList();

    /**
     * Updates the filter of the filtered entry list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Entry> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoAddressBook();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoAddressBook();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoAddressBook();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoAddressBook();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitAddressBook();

    /**
     * Selected entry in the filtered entry list.
     * null if no entry is selected.
     */
    ReadOnlyProperty<Entry> selectedPersonProperty();

    /**
     * Returns the selected entry in the filtered entry list.
     * null if no entry is selected.
     */
    Entry getSelectedPerson();

    /**
     * Sets the selected entry in the filtered entry list.
     */
    void setSelectedPerson(Entry entry);
}
