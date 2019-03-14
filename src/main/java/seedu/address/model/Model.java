package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.entry.Entry;
import seedu.address.storage.Storage;
import seedu.address.ui.ViewMode;

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
     * Gets the storage backing this model.
     */
    Storage getStorage();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' entry book file path.
     */
    Path getEntryBookFilePath();

    /**
     * Sets the user prefs' entry book file path.
     */
    void setEntryBookFilePath(Path entryBookFilePath);

    /**
     * Returns the user prefs' article data directory path.
     */
    Path getArticleDataDirectoryPath();

    /**
     * Sets the user prefs' article data directory path.
     */
    void setArticleDataDirectoryPath(Path articleDataDirectoryPath);
    /**
     * Replaces entry book data with the data in {@code entryBook}.
     */
    void setEntryBook(ReadOnlyEntryBook entryBook);

    /** Returns the EntryBook */
    ReadOnlyEntryBook getEntryBook();

    /**
     * Returns true if a entry with the same identity as {@code entry} exists in the entry book.
     */
    boolean hasEntry(Entry entry);

    /**
     * Deletes the given entry.
     * The entry must exist in the entry book.
     */
    void deleteEntry(Entry target);

    /**
     * Adds the given entry.
     * {@code entry} must not already exist in the entry book.
     */
    void addEntry(Entry entry);

    /**
     * Replaces the given entry {@code target} with {@code editedEntry}.
     * {@code target} must exist in the entry book.
     * The entry identity of {@code editedEntry} must not be the same as another existing entry in the entry book.
     */
    void setEntry(Entry target, Entry editedEntry);

    /**
     * Clears the entire entry book.
     */
    void clearEntryBook();

    /** Returns an unmodifiable view of the filtered entry list */
    ObservableList<Entry> getFilteredEntryList();

    /**
     * Updates the filter of the filtered entry list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEntryList(Predicate<Entry> predicate);

    /**
     * Selected entry in the filtered entry list.
     * null if no entry is selected.
     */
    ReadOnlyProperty<Entry> selectedEntryProperty();

    /**
     * Returns the selected entry in the filtered entry list.
     * null if no entry is selected.
     */
    Entry getSelectedEntry();

    /**
     * Sets the selected entry in the filtered entry list.
     */
    void setSelectedEntry(Entry entry);

    /**
     * Current view mode.
     */
    ReadOnlyProperty<ViewMode> viewModeProperty();

    /**
     * Returns the current view mode.
     */
    ViewMode getViewMode();

    /**
     * Sets the view mode.
     */
    void setViewMode(ViewMode viewMode);

    /**
     * Propagated exception.
     * null if no exception.
     */
    ReadOnlyProperty<Exception> exceptionProperty();

    /**
     * Returns the propagated exception.
     * null if no exception.
     */
    Exception getException();

    /**
     * Sets the exception to be propagated.
     */
    void setException(Exception exceptionToBePropagated);

    /**
     * Command result from manual setting.
     * null if no manually set command result.
     * FUTURE: Command result from latest command execution or manual setting.
     */
    ReadOnlyProperty<CommandResult> commandResultProperty();

    /**
     * Returns the manually set command result.
     * null if no manually set command result.
     */
    CommandResult getCommandResult();

    /**
     * Sets the command result manually.
     */
    void setCommandResult(CommandResult result);

    /**
     * Makes a copy of the model.
     *
     * Mainly created because clone is needed a lot in tests.
     */
    Model clone();
}
