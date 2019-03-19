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
    private EntryBookStorage entryBookStorage;
    private UserPrefsStorage userPrefsStorage;
    private ArticleStorage articleStorage;


    public StorageManager(
            EntryBookStorage entryBookStorage,
            UserPrefsStorage userPrefsStorage,
            ArticleStorage articleStorage) {
        super();
        this.entryBookStorage = entryBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.articleStorage = articleStorage;
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


    // ================ EntryBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return entryBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyEntryBook> readAddressBook() throws DataConversionException, IOException {
        return readAddressBook(entryBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyEntryBook> readAddressBook(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return entryBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyEntryBook addressBook) throws IOException {
        saveAddressBook(addressBook, entryBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyEntryBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        entryBookStorage.saveAddressBook(addressBook, filePath);
    }

    // ================ article methods ================================

    @Override
    public Path getArticleDataDirectoryPath() {
        return articleStorage.getArticleDataDirectoryPath();
    }

    @Override
    public Optional<Path> addArticle(String url, byte[] content) throws IOException {
        return articleStorage.addArticle(url, content);
    }

    @Override
    public Path getArticlePath(String url) {
        return articleStorage.getArticlePath(url);
    }
}
