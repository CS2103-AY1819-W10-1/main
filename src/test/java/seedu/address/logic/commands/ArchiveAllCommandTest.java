package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.testutil.TypicalEntries;

public class ArchiveAllCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allEntriesArchivedSuccessfully() throws Exception {
        Model model = new TypicalModelManagerStub();

        int numEntries = model.getFilteredEntryList().size();
        CommandResult commandResult = new ArchiveAllCommand().execute(model, commandHistory);

        assertEquals(
            String.format(ArchiveAllCommand.MESSAGE_SUCCESS, numEntries),
            commandResult.getFeedbackToUser());
        assertTrue(
            model.getFilteredEntryList().stream()
                .allMatch(model::hasEntry)
        );

        // Executing the command again results in no entries archived because they are all duplicates
        model.setListEntryBook(TypicalEntries.getTypicalListEntryBook());
        commandResult = new ArchiveAllCommand().execute(model, commandHistory);

        assertEquals(
            String.format(ArchiveAllCommand.MESSAGE_SUCCESS, 0),
            commandResult.getFeedbackToUser());
        assertTrue(
            model.getFilteredEntryList().stream()
                .allMatch(model::hasEntry)
        );
    }

}
