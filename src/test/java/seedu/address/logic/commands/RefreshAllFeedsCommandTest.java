package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalEntries.EMPTY_FEED_ENTRY;
import static seedu.address.testutil.TypicalEntries.NOT_A_FEED_ENTRY;
import static seedu.address.testutil.TypicalEntries.ONE_ITEM_FEED_ENTRY;
import static seedu.address.testutil.TypicalEntries.REMOTE_WIKIPEDIA_ENTRY;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.mocks.TemporaryStorageManager;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;

public class RefreshAllFeedsCommandTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFeedsRefreshedSuccessfully() throws Exception {
        Model model = new ModelManagerStub(new TemporaryStorageManager(temporaryFolder));
        model.setContext(ModelContext.CONTEXT_FEEDS);
        model.addFeedsEntry(EMPTY_FEED_ENTRY);
        model.addFeedsEntry(ONE_ITEM_FEED_ENTRY);

        // Check that the wikipedia entry inside ONE_ITEM_FEED_ENTRY has not been added
        assertFalse(model.hasEntry(REMOTE_WIKIPEDIA_ENTRY));

        CommandResult commandResult = new RefreshAllFeedsCommand().execute(model, commandHistory);

        assertEquals(
            String.format(RefreshAllFeedsCommand.MESSAGE_SUCCESS, model.getFilteredEntryList().size()),
            commandResult.getFeedbackToUser());

        // Check that the wikipedia entry inside ONE_ITEM_FEED_ENTRY is now in reading list
        assertTrue(model.hasEntry(REMOTE_WIKIPEDIA_ENTRY));
    }

    @Test
    public void execute_someEntriesRefreshedSuccessfully() throws Exception {
        Model model = new ModelManagerStub(new TemporaryStorageManager(temporaryFolder));
        model.setContext(ModelContext.CONTEXT_FEEDS);
        model.addFeedsEntry(EMPTY_FEED_ENTRY);
        model.addFeedsEntry(ONE_ITEM_FEED_ENTRY);
        int numValidLinks = model.getFilteredEntryList().size();
        model.addFeedsEntry(NOT_A_FEED_ENTRY);

        // Check that the wikipedia entry inside ONE_ITEM_FEED_ENTRY has not been added
        assertFalse(model.hasEntry(REMOTE_WIKIPEDIA_ENTRY));

        CommandResult commandResult = new RefreshAllFeedsCommand().execute(model, commandHistory);

        assertEquals(String.format(
            RefreshAllFeedsCommand.MESSAGE_PARTIAL_SUCCESS,
            numValidLinks,
            numValidLinks + 1,
            NOT_A_FEED_ENTRY.getLink().value),
            commandResult.getFeedbackToUser());

        // Check that the wikipedia entry inside ONE_ITEM_FEED_ENTRY is now added
        assertTrue(model.hasEntry(REMOTE_WIKIPEDIA_ENTRY));
    }

    @Test
    public void execute_someEntriesRefreshedSuccessfully_stopAtFirstError() throws Exception {
        Model model = new ModelManagerStub(new TemporaryStorageManager(temporaryFolder));
        model.setContext(ModelContext.CONTEXT_FEEDS);
        model.addFeedsEntry(EMPTY_FEED_ENTRY);
        int numValidLinks = model.getFilteredEntryList().size();
        model.addFeedsEntry(NOT_A_FEED_ENTRY);
        model.addFeedsEntry(ONE_ITEM_FEED_ENTRY);

        // Check that the wikipedia entry inside ONE_ITEM_FEED_ENTRY has not been added
        assertFalse(model.hasEntry(REMOTE_WIKIPEDIA_ENTRY));

        CommandResult commandResult = new RefreshAllFeedsCommand().execute(model, commandHistory);

        assertEquals(String.format(
            RefreshAllFeedsCommand.MESSAGE_PARTIAL_SUCCESS,
            numValidLinks,
            numValidLinks + 1,
            NOT_A_FEED_ENTRY.getLink().value),
            commandResult.getFeedbackToUser());

        // Check that the wikipedia entry inside ONE_ITEM_FEED_ENTRY has not been added
        assertFalse(model.hasEntry(REMOTE_WIKIPEDIA_ENTRY));
    }

    @Test
    public void execute_noEntriesRefreshedSuccessfully() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(RefreshAllFeedsCommand.MESSAGE_FAILURE);

        Model model = new ModelManagerStub(new TemporaryStorageManager(temporaryFolder));
        model.setContext(ModelContext.CONTEXT_FEEDS);
        model.addFeedsEntry(NOT_A_FEED_ENTRY);
        model.addFeedsEntry(ONE_ITEM_FEED_ENTRY);

        new RefreshAllFeedsCommand().execute(model, commandHistory);
    }

}
