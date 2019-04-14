package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showEntryAtIndex;
import static seedu.address.testutil.TypicalEntries.BROWSER_PANEL_TEST_ENTRY;
import static seedu.address.testutil.TypicalEntries.WIKIPEDIA_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.mocks.TemporaryStorageManager;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.EntryBookBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code RefreshEntryCommand}.
 */
public class RefreshEntryCommandTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private CommandHistory commandHistory = new CommandHistory();

    private Model model;

    @Before
    public void setUp() throws IOException {
        model = makeTestModelWithLocallyLinkedEntries();
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws IOException {
        RefreshEntryCommand refreshCommand = new RefreshEntryCommand(INDEX_FIRST_ENTRY);
        Entry entryToRefresh = model.getFilteredEntryList().get(INDEX_FIRST_ENTRY.getZeroBased());
        URL urlToRefresh = entryToRefresh.getLink().value;

        String expectedMessage = String.format(
            RefreshEntryCommand.MESSAGE_REFRESH_ENTRY_SUCCESS,
            INDEX_FIRST_ENTRY.getOneBased());

        assertFalse(model.hasOfflineCopy(urlToRefresh));
        assertCommandSuccess(refreshCommand, model, commandHistory, expectedMessage);
        assertTrue(model.hasOfflineCopy(urlToRefresh));
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEntryList().size() + 1);
        RefreshEntryCommand refreshCommand = new RefreshEntryCommand(outOfBoundIndex);

        assertCommandFailure(refreshCommand, model, commandHistory, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);

        RefreshEntryCommand refreshCommand = new RefreshEntryCommand(INDEX_FIRST_ENTRY);
        Entry entryToRefresh = model.getFilteredEntryList().get(INDEX_FIRST_ENTRY.getZeroBased());
        URL urlToRefresh = entryToRefresh.getLink().value;

        String expectedMessage = String.format(
            RefreshEntryCommand.MESSAGE_REFRESH_ENTRY_SUCCESS,
            INDEX_FIRST_ENTRY.getOneBased());

        assertFalse(model.hasOfflineCopy(urlToRefresh));
        assertCommandSuccess(refreshCommand, model, commandHistory, expectedMessage);
        assertTrue(model.hasOfflineCopy(urlToRefresh));
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showEntryAtIndex(model, INDEX_FIRST_ENTRY);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getListEntryBook().getEntryList().size());

        RefreshEntryCommand refreshCommand = new RefreshEntryCommand(outOfBoundIndex);

        assertCommandFailure(refreshCommand, model, commandHistory, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RefreshEntryCommand refreshFirstCommand = new RefreshEntryCommand(INDEX_FIRST_ENTRY);
        RefreshEntryCommand refreshSecondCommand = new RefreshEntryCommand(INDEX_SECOND_ENTRY);

        // same object -> returns true
        assertTrue(refreshFirstCommand.equals(refreshFirstCommand));

        // same values -> returns true
        RefreshEntryCommand refreshFirstCommandCopy = new RefreshEntryCommand(INDEX_FIRST_ENTRY);
        assertTrue(refreshFirstCommand.equals(refreshFirstCommandCopy));

        // different types -> returns false
        assertFalse(refreshFirstCommand.equals(1));

        // null -> returns false
        assertFalse(refreshFirstCommand.equals(null));

        // different entry -> returns false
        assertFalse(refreshFirstCommand.equals(refreshSecondCommand));
    }

    /**
     * Returns a model with entries that are locally linked.
     */
    private Model makeTestModelWithLocallyLinkedEntries() throws IOException {
        Model model = new ModelManagerStub(new TemporaryStorageManager(temporaryFolder));
        EntryBook testEntryBook = new EntryBookBuilder()
            .withEntry(WIKIPEDIA_ENTRY)
            .withEntry(BROWSER_PANEL_TEST_ENTRY)
            .build();
        model.setListEntryBook(testEntryBook);
        return model;
    }
}
