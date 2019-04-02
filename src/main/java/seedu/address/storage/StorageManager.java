package seedu.address.storage;

import java.io.IOException;
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
    private UserPrefsStorage userPrefsStorage;
    private ArticleStorage articleStorage;


    public StorageManager(
        EntryBookStorage listEntryBookStorage,
        EntryBookStorage archivesEntryBookStorage,
        UserPrefsStorage userPrefsStorage,
        ArticleStorage articleStorage) {
        super();
        this.listEntryBookStorage = listEntryBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.articleStorage = articleStorage;
        this.archivesEntryBookStorage = archivesEntryBookStorage;
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


    // ================ List EntryBook methods ==============================

    @Override
    public Path getListEntryBookFilePath() {
        return listEntryBookStorage.getEntryBookFilePath();
    }

    @Override
    public Optional<ReadOnlyEntryBook> readListEntryBook() throws DataConversionException, IOException {
        return readListEntryBook(listEntryBookStorage.getEntryBookFilePath());
    }

    private Optional<ReadOnlyEntryBook> readListEntryBook(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return listEntryBookStorage.readEntryBook(filePath);
    }

    @Override
    public void saveListEntryBook(ReadOnlyEntryBook listEntryBook) throws IOException {
        saveListEntryBook(listEntryBook, listEntryBookStorage.getEntryBookFilePath());
    }

    private void saveListEntryBook(ReadOnlyEntryBook listEntryBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        listEntryBookStorage.saveEntryBook(listEntryBook, filePath);
    }

    // ================ Archives EntryBook methods ==============================

    @Override
    public Path getArchivesEntryBookFilePath() {
        return archivesEntryBookStorage.getEntryBookFilePath();
    }

    @Override
    public Optional<ReadOnlyEntryBook> readArchivesEntryBook() throws DataConversionException, IOException {
        return readArchivesEntryBook(archivesEntryBookStorage.getEntryBookFilePath());
    }

    private Optional<ReadOnlyEntryBook> readArchivesEntryBook(Path filePath)
        throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return archivesEntryBookStorage.readEntryBook(filePath);
    }

    @Override
    public void saveArchivesEntryBook(ReadOnlyEntryBook archivesEntryBook) throws IOException {
        saveArchivesEntryBook(archivesEntryBook,
            archivesEntryBookStorage.getEntryBookFilePath());
    }

    private void saveArchivesEntryBook(ReadOnlyEntryBook archivesEntryBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        archivesEntryBookStorage.saveEntryBook(archivesEntryBook, filePath);
    }

    // ================ Article methods ================================

    @Override
    public Path getArticleDataDirectoryPath() {
        return articleStorage.getArticleDataDirectoryPath();
    }

    @Override
    public void deleteArticle(String url) throws IOException {
        articleStorage.deleteArticle(url);
    }

    @Override
    public Optional<Path> addArticle(String url, byte[] content) throws IOException {
        return articleStorage.addArticle(url, content);
    }

    @Override
    public Path getArticlePath(String url) {
        return articleStorage.getArticlePath(url);
    }

    @Override
    public Optional<Path> getOfflineLink(String url) {
        return articleStorage.getOfflineLink(url);
    }
}
