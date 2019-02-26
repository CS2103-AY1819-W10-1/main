package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;

/**
 * Represents a storage for {@link EntryBook}.
 */
public interface EntryBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getAddressBookFilePath();

    /**
     * Returns EntryBook data as a {@link ReadOnlyEntryBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyEntryBook> readAddressBook() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyEntryBook> readAddressBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyEntryBook} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlyEntryBook addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyEntryBook)
     */
    void saveAddressBook(ReadOnlyEntryBook addressBook, Path filePath) throws IOException;

}
