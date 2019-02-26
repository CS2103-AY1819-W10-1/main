package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedEntryBook versionedAddressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Entry> filteredEntries;
    private final SimpleObjectProperty<Entry> selectedPerson = new SimpleObjectProperty<>();

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyEntryBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedAddressBook = new VersionedEntryBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredEntries = new FilteredList<>(versionedAddressBook.getPersonList());
        filteredEntries.addListener(this::ensureSelectedPersonIsValid);
    }

    public ModelManager() {
        this(new EntryBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== EntryBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyEntryBook addressBook) {
        versionedAddressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyEntryBook getAddressBook() {
        return versionedAddressBook;
    }

    @Override
    public boolean hasPerson(Entry entry) {
        requireNonNull(entry);
        return versionedAddressBook.hasPerson(entry);
    }

    @Override
    public void deletePerson(Entry target) {
        versionedAddressBook.removePerson(target);
    }

    @Override
    public void addPerson(Entry entry) {
        versionedAddressBook.addPerson(entry);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Entry target, Entry editedEntry) {
        requireAllNonNull(target, editedEntry);

        versionedAddressBook.setPerson(target, editedEntry);
    }

    //=========== Filtered Entry List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Entry} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Entry> getFilteredPersonList() {
        return filteredEntries;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredEntries.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    //=========== Selected entry ===========================================================================

    @Override
    public ReadOnlyProperty<Entry> selectedPersonProperty() {
        return selectedPerson;
    }

    @Override
    public Entry getSelectedPerson() {
        return selectedPerson.getValue();
    }

    @Override
    public void setSelectedPerson(Entry entry) {
        if (entry != null && !filteredEntries.contains(entry)) {
            throw new EntryNotFoundException();
        }
        selectedPerson.setValue(entry);
    }

    /**
     * Ensures {@code selectedPerson} is a valid entry in {@code filteredEntries}.
     */
    private void ensureSelectedPersonIsValid(ListChangeListener.Change<? extends Entry> change) {
        while (change.next()) {
            if (selectedPerson.getValue() == null) {
                // null is always a valid selected entry, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedPersonReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedPerson.getValue());
            if (wasSelectedPersonReplaced) {
                // Update selectedPerson to its new value.
                int index = change.getRemoved().indexOf(selectedPerson.getValue());
                selectedPerson.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedPersonRemoved = change.getRemoved().stream()
                    .anyMatch(removedPerson -> selectedPerson.getValue().isSamePerson(removedPerson));
            if (wasSelectedPersonRemoved) {
                // Select the entry that came before it in the list,
                // or clear the selection if there is no such entry.
                selectedPerson.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredEntries.equals(other.filteredEntries)
                && Objects.equals(selectedPerson.get(), other.selectedPerson.get());
    }

}
