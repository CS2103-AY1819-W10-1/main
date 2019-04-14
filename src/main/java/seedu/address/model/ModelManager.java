package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;
import seedu.address.storage.Storage;
import seedu.address.ui.ViewMode;
import seedu.address.util.Network;

/**
 * Represents the in-memory model of the entry book data.
 */
public class ModelManager implements Model {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final EntryBook listEntryBook;
    private final EntryBook archivesEntryBook;
    private final EntryBook searchEntryBook = new EntryBook();
    private final EntryBook feedsEntryBook;
    private final UserPrefs userPrefs;

    private final SimpleListProperty<Entry> displayedEntryList;
    private final FilteredList<Entry> filteredEntries;
    private final SimpleObjectProperty<Entry> selectedEntry = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<ViewMode> currentViewMode = new SimpleObjectProperty<>(new ViewMode());
    private final SimpleObjectProperty<Exception> exception = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<CommandResult> commandResult = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<ModelContext> context = new SimpleObjectProperty<>(ModelContext.CONTEXT_LIST);
    private final Storage storage;

    /**
     * Initializes a ModelManager with the given listEntryBook, userPrefs, and storage
     */
    public ModelManager(ReadOnlyEntryBook listEntryBook,
                        ReadOnlyEntryBook archivesEntryBook,
                        ReadOnlyEntryBook feedEntryBook,
                        ReadOnlyUserPrefs userPrefs,
                        Storage storage) {
        super();
        requireAllNonNull(listEntryBook, userPrefs, storage);

        logger.fine("Initializing with list context entry book: " + listEntryBook + " and user prefs " + userPrefs);

        this.listEntryBook = new EntryBook(listEntryBook);
        this.archivesEntryBook = new EntryBook(archivesEntryBook);
        this.feedsEntryBook = new EntryBook(feedEntryBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.storage = storage;

        displayedEntryList = new SimpleListProperty<>(this.listEntryBook.getEntryList());
        filteredEntries = new FilteredList<>(this.displayedEntryList);

        setUpListeners();
    }

    private void setUpListeners() {
        // Save the relevant entry books to storage whenever they are modified.
        listEntryBook.addListener(observable -> saveListEntryBookToStorageListener());
        archivesEntryBook.addListener(observable -> saveArchivesEntryBookToStorageListener());
        feedsEntryBook.addListener(obserable -> saveFeedsEntryBookToStorageListener());

        // Updates selected entry to a valid selection (or none) whenever filtered entries is modified.
        filteredEntries.addListener(this::ensureSelectedEntryIsValid);

        // Updates displayed entry list whenever the context of the Model changes.
        context.addListener((observable, oldContext, newContext) -> {
                switch (newContext) {
                case CONTEXT_LIST:
                    displayEntryBook(listEntryBook);
                    break;
                case CONTEXT_ARCHIVES:
                    displayEntryBook(archivesEntryBook);
                    break;
                case CONTEXT_SEARCH:
                    displayEntryBook(searchEntryBook);
                    break;
                case CONTEXT_FEEDS:
                    displayEntryBook(feedsEntryBook);
                    break;
                default:
                }
                updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
            }
        );
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
    public Path getListEntryBookFilePath() {
        return userPrefs.getListEntryBookFilePath();
    }

    @Override
    public void setListEntryBookFilePath(Path listEntryBookFilePath) {
        requireNonNull(listEntryBookFilePath);
        userPrefs.setListEntryBookFilePath(listEntryBookFilePath);
    }

    @Override
    public Path getArticleDataDirectoryPath() {
        return userPrefs.getArticleDataDirectoryPath();
    }

    @Override
    public void setArticleDataDirectoryPath(Path articleDataDirectoryPath) {
        requireNonNull(articleDataDirectoryPath);
        userPrefs.setArticleDataDirectoryPath(articleDataDirectoryPath);
    }

    @Override
    public Path getArchivesEntryBookFilePath() {
        return userPrefs.getArchivesEntryBookFilePath();
    }

    @Override
    public void setArchivesEntryBookFilePath(Path archivesEntryBookFilePath) {
        requireNonNull(archivesEntryBookFilePath);
        userPrefs.setArchivesEntryBookFilePath(archivesEntryBookFilePath);
    }

    @Override
    public boolean hasOfflineCopy(URL url) {
        return getOfflineLink(url).isPresent();
    }

    @Override
    public Optional<URL> getOfflineLink(URL url) {
        return storage.getOfflineLink(url).flatMap(path -> {
            try {
                return Optional.of(path.toUri().toURL());
            } catch (MalformedURLException e) {
                return Optional.empty();
            }
        });
    }

    //=========== EntryBook ================================================================================

    @Override
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return hasListEntry(entry) || hasArchivesEntry(entry);
    }

    //=========== List EntryBook ================================================================================

    @Override
    public void setListEntryBook(ReadOnlyEntryBook listEntryBook) {
        this.listEntryBook.resetData(listEntryBook);
    }

    @Override
    public ReadOnlyEntryBook getListEntryBook() {
        return listEntryBook;
    }

    @Override
    public boolean hasListEntry(Entry listEntry) {
        requireNonNull(listEntry);
        return listEntryBook.hasEntry(listEntry);
    }

    @Override
    public void deleteListEntry(Entry target) {
        try {
            this.deleteArticle(target.getLink().value);
        } catch (IOException ioe) {
            // If there was a problem deleting the file,
            // do nothing because that either means
            // the file didn't exist to begin with
            // or we are in some really deep OS-related system error.
        }
        listEntryBook.removeEntry(target);
    }

    @Override
    public void addListEntry(Entry entry, Optional<byte[]> articleContent) {
        if (articleContent.isPresent()) {
            try {
                this.addArticle(entry.getLink().value, articleContent.get());
            } catch (IOException ioe) {
                // Do nothing if failed to save content to disk
            }
        }
        listEntryBook.addEntry(entry);
    }

    @Override
    public void setListEntry(Entry target, Entry editedEntry) {
        requireAllNonNull(target, editedEntry);

        listEntryBook.setEntry(target, editedEntry);
    }

    @Override
    public void clearListEntryBook() {
        listEntryBook.clear();
    }

    @Override
    public void refreshEntry(Entry entry, byte[] articleContent) {
        try {
            this.addArticle(entry.getLink().value, articleContent);
        } catch (IOException ioe) {
            // Do nothing if failed to save content to disk for now
        }
    }

    //=========== Archives EntryBook ================================================================================

    @Override
    public void setArchivesEntryBook(ReadOnlyEntryBook archivesEntryBook) {
        this.archivesEntryBook.resetData(archivesEntryBook);
    }

    @Override
    public ReadOnlyEntryBook getArchivesEntryBook() {
        return archivesEntryBook;
    }

    @Override
    public boolean hasArchivesEntry(Entry archiveEntry) {
        requireNonNull(archiveEntry);
        return archivesEntryBook.hasEntry(archiveEntry);
    }

    @Override
    public void deleteArchivesEntry(Entry target) {
        archivesEntryBook.removeEntry(target);
    }

    @Override
    public void addArchivesEntry(Entry entry) {
        archivesEntryBook.addEntry(entry);
    }

    @Override
    public void clearArchivesEntryBook() {
        archivesEntryBook.clear();
    }

    //=========== Feeds EntryBook ============================================================================

    @Override
    public void setFeedsEntryBook(ReadOnlyEntryBook feedsEntryBook) {
        this.feedsEntryBook.resetData(feedsEntryBook);
    }

    @Override
    public ReadOnlyEntryBook getFeedsEntryBook() {
        return feedsEntryBook;
    }

    @Override
    public boolean hasFeedsEntry(Entry feed) {
        requireNonNull(feed);
        return feedsEntryBook.hasEntry(feed);
    }

    @Override
    public void deleteFeedsEntry(Entry target) {
        feedsEntryBook.removeEntry(target);
    }

    @Override
    public void addFeedsEntry(Entry feed) {
        feedsEntryBook.addEntry(feed);
    }

    @Override
    public void clearFeedsEntryBook() {
        feedsEntryBook.clear();
    }

    //=========== Search EntryBook ==========================================================================

    @Override
    public void setSearchEntryBook(ReadOnlyEntryBook searchEntryBook) {
        this.searchEntryBook.resetData(searchEntryBook);
    }

    //=========== Storage ===================================================================================

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public void deleteArticle(URL url) throws IOException {
        storage.deleteArticle(url);
    }

    @Override
    public Optional<Path> addArticle(URL url, byte[] articleContent) throws IOException {
        return storage.addArticle(url, articleContent);
    }

    @Override
    public Optional<String> getArticle(URL url) {
        return storage.getArticle(url);
    }

    //=========== Displayed Entry List ================================================================================

    private void displayEntryBook(ReadOnlyEntryBook entryBook) {
        displayedEntryList.set(entryBook.getEntryList());
    }

    //=========== Filtered Entry List =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Entry} backed by the internal list of
     * {@code listEntryBook}
     */
    @Override
    public ObservableList<Entry> getFilteredEntryList() {
        return filteredEntries;
    }

    @Override
    public void updateFilteredEntryList(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredEntries.setPredicate(predicate);
    }

    //=========== Selected entry ===========================================================================

    @Override
    public ReadOnlyProperty<Entry> selectedEntryProperty() {
        return selectedEntry;
    }

    @Override
    public Entry getSelectedEntry() {
        return selectedEntry.getValue();
    }

    @Override
    public void setSelectedEntry(Entry entry) {
        if (entry != null && !filteredEntries.contains(entry)) {
            throw new EntryNotFoundException();
        }
        selectedEntry.setValue(entry);
        if (getContext() == ModelContext.CONTEXT_LIST && entry != null) {
            logger.info("Ensuring selected entry is downloaded: " + entry.getLink().value);
            ensureDownloaded(entry.getLink().value);
        }
    }

    /**
     * Ensures that we have a local copy of the article at the specified url.
     */
    private void ensureDownloaded(URL url) {
        if (!hasOfflineCopy(url)) {
            Network.fetchArticleAsync(url)
                // Ensure model updates are run on JavaFX thread
                .thenAccept(articleContent -> Platform.runLater(() -> {
                    try {
                        logger.info("Auto-downloaded article successfully: " + url);
                        addArticle(url, articleContent);
                    } catch (IOException ioe) {
                        // If couldn't save article, just ignore
                    }
                }));
        }
    }

    //=========== View mode ===========================================================================

    @Override
    public ReadOnlyProperty<ViewMode> viewModeProperty() {
        return currentViewMode;
    }

    @Override
    public ViewMode getViewMode() {
        return currentViewMode.getValue();
    }

    @Override
    public void setViewMode(ViewMode viewMode) {
        currentViewMode.setValue(viewMode);
    }

    //=========== Exception propagation ===========================================================================

    @Override
    public ReadOnlyProperty<Exception> exceptionProperty() {
        return exception;
    }

    @Override
    public Exception getException() {
        return exception.getValue();
    }

    @Override
    public void setException(Exception exceptionToBePropagated) {
        exception.setValue(exceptionToBePropagated);
    }

    //=========== Command result ===========================================================================

    @Override
    public ReadOnlyProperty<CommandResult> commandResultProperty() {
        return commandResult;
    }

    @Override
    public CommandResult getCommandResult() {
        return commandResult.getValue();
    }

    @Override
    public void setCommandResult(CommandResult result) {
        this.commandResult.setValue(result);
    }

    //=========== Context ===========================================================================

    @Override
    public ReadOnlyProperty<ModelContext> contextProperty() {
        return this.context;
    }

    @Override
    public ModelContext getContext() {
        return this.context.getValue();
    }

    @Override
    public void setContext(ModelContext context) {
        this.context.setValue(context);
    }

    /**
     * Ensures {@code selectedEntry} is a valid entry in {@code filteredEntries}.
     */
    private void ensureSelectedEntryIsValid(ListChangeListener.Change<? extends Entry> change) {
        while (change.next()) {
            if (selectedEntry.getValue() == null) {
                // null is always a valid selected entry, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedEntryReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedEntry.getValue());
            if (wasSelectedEntryReplaced) {
                // Update selectedEntry to its new value.
                int index = change.getRemoved().indexOf(selectedEntry.getValue());
                selectedEntry.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedEntryRemoved = change.getRemoved().stream()
                    .anyMatch(removedEntry -> selectedEntry.getValue().isSameEntry(removedEntry));
            if (wasSelectedEntryRemoved) {
                // Select the entry that came before it in the list,
                // or clear the selection if there is no such entry.
                selectedEntry.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    /**
     * Ensures that storage is updated whenever list entry book is modified.
     */
    private void saveListEntryBookToStorageListener() {
        logger.info("Entry book modified, saving to file.");
        try {
            storage.saveListEntryBook(listEntryBook);
        } catch (IOException ioe) {
            setException(new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe));
        }
    }

    /**
     * Ensures that storage is updated whenever archives entry book is modified.
     */
    private void saveArchivesEntryBookToStorageListener() {
        logger.info("Archives modified, saving to file.");
        try {
            storage.saveArchivesEntryBook(archivesEntryBook);
        } catch (IOException ioe) {
            setException(new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe));
        }
    }

    /**
     * Ensures that storage is updated whenever archives entry book is modified.
     */
    private void saveFeedsEntryBookToStorageListener() {
        logger.info("Feed list modified, saving to file.");
        try {
            storage.saveFeedsEntryBook(feedsEntryBook);
        } catch (IOException ioe) {
            setException(new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe));
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

        boolean stateCheck = listEntryBook.equals(other.listEntryBook)
                && archivesEntryBook.equals(other.archivesEntryBook)
                && searchEntryBook.equals(other.searchEntryBook)
                && feedsEntryBook.equals(other.feedsEntryBook)
                && userPrefs.equals(other.userPrefs)
                && displayedEntryList.equals(other.displayedEntryList)
                && filteredEntries.equals(other.filteredEntries)
                && Objects.equals(context.get(), other.context.get())
                && Objects.equals(selectedEntry.get(), other.selectedEntry.get())
                && Objects.equals(currentViewMode.get(), other.currentViewMode.get())
                && Objects.equals(commandResult.get(), other.commandResult.get());

        if (exception.get() == null && other.exception.get() == null) { // both don't have exceptions set
            return stateCheck;
        } else if (exception.get() != null && other.exception.get() != null) { // both have exceptions set
            return stateCheck
                    && exception.get().getClass().equals(other.exception.get().getClass())
                    && exception.get().getMessage().equals(other.exception.get().getMessage());
        } else { // not equal as one has exception set and the other does not
            return false;
        }
    }

    @Override
    public Model clone() {
        Model clonedModel = new ModelManager(this.listEntryBook, this.archivesEntryBook, this.feedsEntryBook,
                this.userPrefs, this.storage);
        clonedModel.setContext(this.getContext());
        return clonedModel;
    }

    @Override
    public void archiveEntry(Entry target) {
        deleteListEntry(target);
        addArchivesEntry(target);
    }

    @Override
    public void unarchiveEntry(Entry entry, Optional<byte[]> articleContent) {
        deleteArchivesEntry(entry);
        addListEntry(entry, articleContent);
    }
}
