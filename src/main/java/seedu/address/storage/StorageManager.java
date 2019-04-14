package seedu.address.storage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of EntryBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private EntryBookStorage listEntryBookStorage;
    private EntryBookStorage archivesEntryBookStorage;
    private EntryBookStorage feedsEntryBookStorage;
    private UserPrefsStorage userPrefsStorage;
    private ArticleStorage articleStorage;


    public StorageManager(
            EntryBookStorage listEntryBookStorage,
            EntryBookStorage archivesEntryBookStorage,
            EntryBookStorage feedsEntryBookStorage,
            UserPrefsStorage userPrefsStorage,
            ArticleStorage articleStorage) {
        super();
        this.listEntryBookStorage = listEntryBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.articleStorage = articleStorage;
        this.archivesEntryBookStorage = archivesEntryBookStorage;
        this.feedsEntryBookStorage = feedsEntryBookStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================= General EntryBook methods ========================

    /**
     * Saves an entryBook to the given entryBookStorage.
     */
    private static void saveEntryBook(ReadOnlyEntryBook entryBook, EntryBookStorage entryBookStorage)
            throws IOException {

        Path filePath = entryBookStorage.getEntryBookFilePath();
        logger.fine("Attempting to write to data file: " + filePath);
        entryBookStorage.saveEntryBook(entryBook, filePath);
    }

    /**
     * Reads an EntryBook from the EntryBookStorage.
     */
    private static Optional<ReadOnlyEntryBook> readEntryBook(EntryBookStorage entryBookStorage)
            throws IOException, DataConversionException {

        Path filePath = entryBookStorage.getEntryBookFilePath();
        logger.fine("Attempting to read data from file: " + filePath);
        return entryBookStorage.readEntryBook(filePath);
    }


    // ================ List EntryBook methods ==============================

    @Override
    public Path getListEntryBookFilePath() {
        return listEntryBookStorage.getEntryBookFilePath();
    }

    @Override
    public Optional<ReadOnlyEntryBook> readListEntryBook() throws DataConversionException, IOException {
        return readEntryBook(listEntryBookStorage);
    }

    @Override
    public void saveListEntryBook(ReadOnlyEntryBook listEntryBook) throws IOException {
        saveEntryBook(listEntryBook, listEntryBookStorage);
    }

    // ================ Archives EntryBook methods ==============================

    @Override
    public Path getArchivesEntryBookFilePath() {
        return archivesEntryBookStorage.getEntryBookFilePath();
    }

    @Override
    public Optional<ReadOnlyEntryBook> readArchivesEntryBook() throws DataConversionException, IOException {
        return readEntryBook(archivesEntryBookStorage);
    }

    @Override
    public void saveArchivesEntryBook(ReadOnlyEntryBook archivesEntryBook) throws IOException {
        saveEntryBook(archivesEntryBook, archivesEntryBookStorage);
    }

    // ================ Feeds EntryBook methods ===================================

    @Override
    public Path getFeedsEntryBookFilePath() {
        return feedsEntryBookStorage.getEntryBookFilePath();
    }

    @Override
    public Optional<ReadOnlyEntryBook> readFeedsEntryBook() throws DataConversionException, IOException {
        return readEntryBook(feedsEntryBookStorage);
    }

    @Override
    public void saveFeedsEntryBook(ReadOnlyEntryBook feedsEntryBook) throws IOException {
        saveEntryBook(feedsEntryBook, feedsEntryBookStorage);
    }

    // ================ Article methods ================================

    @Override
    public Path getArticleDataDirectoryPath() {
        return articleStorage.getArticleDataDirectoryPath();
    }

    @Override
    public void deleteArticle(URL url) throws IOException {
        articleStorage.deleteArticle(url);
    }

    @Override
    public Optional<Path> addArticle(URL url, byte[] content) throws IOException {
        return articleStorage.addArticle(url, content);
    }

    @Override
    public Path getArticlePath(URL url) {
        return articleStorage.getArticlePath(url);
    }

    @Override
    public Optional<Path> getOfflineLink(URL url) {
        return articleStorage.getOfflineLink(url);
    }

    @Override
    public Optional<String> getArticle(URL url) {
        return articleStorage.getArticle(url);
    }

}
