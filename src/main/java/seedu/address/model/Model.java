package seedu.address.model;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
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
    Predicate<Entry> PREDICATE_SHOW_ALL_ENTRIES = unused -> true;

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
     * Returns the user prefs' list entry book file path.
     */
    Path getListEntryBookFilePath();

    /**
     * Sets the user prefs' list entry book file path.
     */
    void setListEntryBookFilePath(Path listEntryBookFilePath);

    /**
     * Returns the user prefs' archives entry book file path.
     */
    Path getArchivesEntryBookFilePath();

    /**
     * Sets the user prefs' archives entry book file path.
     */
    void setArchivesEntryBookFilePath(Path archivesEntryBookFilePath);

    /**
     * Returns the user prefs' article data directory path.
     */
    Path getArticleDataDirectoryPath();

    /**
     * Returns the link to the offline copy of the url given if it exists.
     */
    Optional<URL> getOfflineLink(URL url);

    /**
     * Sets the user prefs' article data directory path.
     */
    void setArticleDataDirectoryPath(Path articleDataDirectoryPath);

    /**
     * Returns true if an entry with the same identity as {@code entry} exists in either the list/archives entry book.
     */
    boolean hasEntry(Entry entry);

    /**
     * Replaces list entry book data with the data in {@code listEntryBook}.
     */
    void setListEntryBook(ReadOnlyEntryBook listEntryBook);

    /** Returns the list entry book */
    ReadOnlyEntryBook getListEntryBook();

    /**
     * Returns true if a list entry with the same identity as {@code entry} exists in the entry book.
     */
    boolean hasListEntry(Entry listEntry);

    /**
     * Deletes the given entry.
     * The entry must exist in the list entry book.
     */
    void deleteListEntry(Entry target);

    /**
     * Adds the given entry.
     * {@code entry} must not already exist in the list entry book.
     */
    void addListEntry(Entry entry, Optional<byte[]> articleContent);

    /**
     * Replaces the given entry {@code target} with {@code editedEntry}.
     * {@code target} must exist in the list entry book.
     * The entry identity of {@code editedEntry} must not be the same as another existing entry in the list entry book.
     */
    void setListEntry(Entry target, Entry editedEntry);

    /**
     * Clears the entire list entry book.
     */
    void clearListEntryBook();

    /**
     * Replaces archives entry book data with the data in {@code archivesEntryBook}.
     */
    void setArchivesEntryBook(ReadOnlyEntryBook archivesEntryBook);

    /**
     * Returns the archives entry book.
     */
    ReadOnlyEntryBook getArchivesEntryBook();

    /**
     * Returns true if an entry with the same identity as {@code archiveEntry} exists in the archives entry book.
     */
    boolean hasArchivesEntry(Entry archiveEntry);

    /**
     * Deletes the given entry.
     * The entry must exist in the archives entry book.
     */
    void deleteArchivesEntry(Entry target);

    /**
     * Adds the given archives entry.
     * {@code entry} must not already exist in the archives entry book.
     */
    void addArchivesEntry(Entry entry);

    /**
     * Clears the entire archives entry book.
     */
    void clearArchivesEntryBook();

    /** Returns the feeds entry book. */
    ReadOnlyEntryBook getFeedsEntryBook();

    /** Returns whether feed is in the feeds entry book. */
    boolean hasFeedsEntry(Entry feed);

    /**
     * Removes the target from feed entry book.
     * Target must exist in feed entry book.
     */
    void deleteFeedsEntry(Entry target);

    /** Adds feed to the feed entry book. It should not already be there. */
    void addFeedsEntry(Entry feed);

    /** Clears the entire feed entry book. */
    void clearFeedsEntryBook();

    /** Deletes article associated with {@code url}. */
    void deleteArticle(URL url) throws IOException;

    /** Sets the search context entry book. */
    void setSearchEntryBook(ReadOnlyEntryBook searchEntryBook);

    /** Adds article with {@code articleContent} associated with {@code url}. */
    Optional<Path> addArticle(URL url, byte[] articleContent) throws IOException;

    /** Gets article associated with {@code url}. */
    Optional<String> getArticle(URL url);

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
     * Current context of the model.
     */
    ReadOnlyProperty<ModelContext> contextProperty();

    /**
     * Returns the context of the model.
     */
    ModelContext getContext();

    /**
     * Sets the context of the model.
     * @param context
     */
    void setContext(ModelContext context);

    /**
     * Makes a copy of the model.
     *
     * Mainly created because clone is needed a lot in tests.
     */
    Model clone();

    /**
     * Archives the given entry.
     * The entry must exist in the entry book.
     */
    void archiveEntry(Entry target);

    /**
     * Un-archives the given entry.
     * The entry must exist in the entry book archives.
     */
    void unarchiveEntry(Entry entry, Optional<byte[]> articleContent);

    /**
     * Refreshes the given entry (re-downloads its saved content).
     * The entry must exist in the entry book list.
     */
    void refreshEntry(Entry entry, byte[] articleContent);
}
