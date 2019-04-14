package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.testutil.TypicalEntries;

public class UnarchiveAllCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allEntriesUnarchivedSuccessfully() throws Exception {
        Model model = new TypicalModelManagerStub();
        model.setContext(ModelContext.CONTEXT_ARCHIVES);

        int numEntries = model.getFilteredEntryList().size();
        CommandResult commandResult = new UnarchiveAllCommand().execute(model, commandHistory);

        assertEquals(
            String.format(UnarchiveAllCommand.MESSAGE_SUCCESS, numEntries),
            commandResult.getFeedbackToUser());
        assertTrue(
            model.getFilteredEntryList().stream()
                .allMatch(model::hasEntry)
        );

        // Executing the command again results in no entries un-archived because they are all duplicates
        model.setArchivesEntryBook(TypicalEntries.getTypicalArchivesEntryBook());
        commandResult = new UnarchiveAllCommand().execute(model, commandHistory);

        assertEquals(
            String.format(UnarchiveAllCommand.MESSAGE_SUCCESS, 0),
            commandResult.getFeedbackToUser());
        assertTrue(
            model.getFilteredEntryList().stream()
                .allMatch(model::hasEntry)
        );
    }

}
