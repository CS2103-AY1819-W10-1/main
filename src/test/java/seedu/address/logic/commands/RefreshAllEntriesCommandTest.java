package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.BROWSER_PANEL_TEST_ENTRY;
import static seedu.address.testutil.TypicalEntries.WIKIPEDIA_ENTRY;

import java.io.IOException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.mocks.TemporaryStorageManager;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.testutil.EntryBookBuilder;

public class RefreshAllEntriesCommandTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allEntriesRefreshedSuccessfully() throws Exception {
        Model model = makeTestModelWithLocallyLinkedEntries();

        assertTrue(
            model.getFilteredEntryList().stream()
                .noneMatch(entry -> model.hasOfflineCopy(entry.getLink().value)));
        CommandResult commandResult = new RefreshAllEntriesCommand().execute(model, commandHistory);

        assertEquals(
            String.format(RefreshAllEntriesCommand.MESSAGE_SUCCESS, model.getFilteredEntryList().size()),
            commandResult.getFeedbackToUser());
        assertTrue(
            model.getFilteredEntryList().stream()
                .allMatch(entry -> model.hasOfflineCopy(entry.getLink().value)));
    }

    @Test
    public void execute_someEntriesRefreshedSuccessfully() throws Exception {
        Model model = makeTestModelWithLocallyLinkedEntries();
        int numValidLinks = model.getFilteredEntryList().size();
        model.addListEntry(ALICE, Optional.empty());

        assertTrue(
            model.getFilteredEntryList().stream()
                .noneMatch(entry -> model.hasOfflineCopy(entry.getLink().value)));
        CommandResult commandResult = new RefreshAllEntriesCommand().execute(model, commandHistory);

        assertEquals(String.format(
            RefreshAllEntriesCommand.MESSAGE_PARTIAL_SUCCESS,
            numValidLinks,
            numValidLinks + 1,
            ALICE.getLink().value),
            commandResult.getFeedbackToUser());
        assertTrue(
            model.getFilteredEntryList().stream()
                .anyMatch(entry -> model.hasOfflineCopy(entry.getLink().value)));
    }

    @Test
    public void execute_noEntriesRefreshedSuccessfully() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(RefreshAllEntriesCommand.MESSAGE_FAILURE);

        Model model = new TypicalModelManagerStub();

        assertTrue(
            model.getFilteredEntryList().stream()
                .noneMatch(entry -> model.hasOfflineCopy(entry.getLink().value)));

        new RefreshAllEntriesCommand().execute(model, commandHistory);
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
