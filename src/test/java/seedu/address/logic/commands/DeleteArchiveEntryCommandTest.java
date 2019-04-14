package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showEntryAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.mocks.StorageStub;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.entry.Entry;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteArchiveEntryCommand}.
 */
public class DeleteArchiveEntryCommandTest {

    private Model model = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model.setContext(ModelContext.CONTEXT_ARCHIVES);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Entry entryToDelete = model.getFilteredEntryList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteArchiveEntryCommand deleteCommand = new DeleteArchiveEntryCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(DeleteArchiveEntryCommand.MESSAGE_DELETE_ENTRY_SUCCESS, entryToDelete);

        ModelManager expectedModel = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(),
            model.getFeedsEntryBook(), new UserPrefs(), new StorageStub());
        expectedModel.setContext(ModelContext.CONTEXT_ARCHIVES);
        expectedModel.deleteArchivesEntry(entryToDelete);

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEntryList().size() + 1);
        DeleteArchiveEntryCommand deleteCommand = new DeleteArchiveEntryCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);

        Entry entryToDelete = model.getFilteredEntryList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteArchiveEntryCommand deleteCommand = new DeleteArchiveEntryCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(DeleteArchiveEntryCommand.MESSAGE_DELETE_ENTRY_SUCCESS, entryToDelete);

        ModelManager expectedModel = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(),
            model.getFeedsEntryBook(), new UserPrefs(), new StorageStub());
        expectedModel.setContext(ModelContext.CONTEXT_ARCHIVES);
        expectedModel.deleteArchivesEntry(entryToDelete);
        showNoEntry(expectedModel);

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getArchivesEntryBook().getEntryList().size());

        DeleteArchiveEntryCommand deleteCommand = new DeleteArchiveEntryCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteArchiveEntryCommand deleteFirstCommand = new DeleteArchiveEntryCommand(INDEX_FIRST_ENTRY);
        DeleteArchiveEntryCommand deleteSecondCommand = new DeleteArchiveEntryCommand(INDEX_SECOND_ENTRY);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteArchiveEntryCommand deleteFirstCommandCopy = new DeleteArchiveEntryCommand(INDEX_FIRST_ENTRY);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different entry -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEntry(Model model) {
        model.updateFilteredEntryList(p -> false);

        assertTrue(model.getFilteredEntryList().isEmpty());
    }
}
