package seedu.address.mocks;

import seedu.address.model.EntryBook;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;

/**
 * A mock to create an empty ModelManager for testing purposes
 */
public class ModelManagerStub extends ModelManager {

    public ModelManagerStub() {
        super(new EntryBook(), new EntryBook(), new EntryBook(), new UserPrefs(), new StorageStub());
    }

    public ModelManagerStub(Storage storage) {
        super(new EntryBook(), new EntryBook(), new EntryBook(), new UserPrefs(), storage);
    }

}
