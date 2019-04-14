package seedu.address.mocks;

import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;

/**
 * A mock for Storage for ease of creating objects in tests
 */
public class StorageStub implements Storage {

    @Override
    public Path getUserPrefsFilePath() {
        return null;
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() {
        return Optional.empty();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
        // Do nothing
    }

    @Override
    public Path getListEntryBookFilePath() {
        return null;
    }

    @Override
    public Optional<ReadOnlyEntryBook> readListEntryBook() {
        return Optional.empty();
    }

    @Override
    public void saveListEntryBook(ReadOnlyEntryBook entryBook) {
        // Do nothing
    }

    @Override
    public Path getArchivesEntryBookFilePath() {
        return null;
    }

    @Override
    public Optional<ReadOnlyEntryBook> readArchivesEntryBook() {
        return Optional.empty();
    }

    @Override
    public void saveArchivesEntryBook(ReadOnlyEntryBook entryBook) {
        // Do nothing
    }

    @Override
    public Path getFeedsEntryBookFilePath() {
        return null;
    }

    @Override
    public Optional<ReadOnlyEntryBook> readFeedsEntryBook() {
        return Optional.empty();
    }

    @Override
    public void saveFeedsEntryBook(ReadOnlyEntryBook entryBook) {
        // Do nothing
    }

    @Override
    public Path getArticleDataDirectoryPath() {
        return null;
    }

    @Override
    public void deleteArticle(URL url) {
        // Do nothing
    }

    @Override
    public Optional<Path> addArticle(URL url, byte[] content) {
        return Optional.empty();
    }

    @Override
    public Path getArticlePath(URL url) {
        return null;
    }

    @Override
    public Optional<Path> getOfflineLink(URL url) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getArticle(URL url) {
        return Optional.empty();
    }
}
